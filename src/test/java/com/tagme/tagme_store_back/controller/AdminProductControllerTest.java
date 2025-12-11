package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.webModel.request.ProductInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.ProductUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.ProductDetailResponse;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.UserRole;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.ProductService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Base64;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminProductController.class)
class AdminProductControllerTest {
    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto productDto1;

    @BeforeEach
    void setUp() throws SQLException {
        productDto1 = Instancio.of(ProductDto.class)
                .supply(field(ProductDto::image), () -> {
                    try {
                        byte[] bytes = "imagen de prueba".getBytes();
                        return new SerialBlob(bytes);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .withSeed(10)
                .create();

        UserDto adminUser = Instancio.of(UserDto.class)
                .set(field(UserDto.class, "role"), UserRole.ADMIN)
                .create();
        when(authService.getByToken(anyString())).thenReturn(adminUser);
    }

    @Nested
    class CreateProductTests {
        @Test
        void createProduct_ShouldReturnCreatedProduct_WhenValidRequest() throws Exception {
            ProductDto createdProduct = Instancio.of(ProductDto.class).withSeed(30).create();
            when(productService.create(any(ProductDto.class))).thenReturn(createdProduct);

            String requestBody = objectMapper.writeValueAsString(
                    Instancio.of(ProductDetailResponse.class).withSeed(30)
                            .set(field(ProductDetailResponse.class, "image"), Base64.getEncoder().encodeToString("imagen prueba".getBytes()))
                            .create());

            mockMvc.perform(post("/admin/products")
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(createdProduct.id()))
                    .andExpect(jsonPath("$.name").value(createdProduct.name()));
        }

        @Test
        void createProduct_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
            String requestBody = objectMapper.writeValueAsString(Instancio.of(ProductInsertRequest.class)
                    .set(field(ProductInsertRequest.class, "name"), null)
                    .withSeed(100)
                    .create());

            mockMvc.perform(post("/admin/products")
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateProductTests {
        @Test
        void updateProduct_ShouldReturnUpdatedProduct_WhenValidRequest() throws Exception {
            ProductDto updatedProduct = Instancio.of(ProductDto.class).withSeed(40).create();
            when(productService.update(any(ProductDto.class))).thenReturn(updatedProduct);


            ProductUpdateRequest productUpdateRequest = Instancio.of(ProductUpdateRequest.class)
                    .set(field(ProductUpdateRequest.class, "image"), Base64.getEncoder().encodeToString("imagen actualizada".getBytes()))
                    .set(field(ProductUpdateRequest.class, "discountPercentage"), BigDecimal.valueOf(100L))
                    .withSeed(40)
                    .create();

            String requestBody = objectMapper.writeValueAsString(productUpdateRequest);

            System.out.println(productUpdateRequest+ " ");
            System.out.println(requestBody+ "sadf");
            mockMvc.perform(put("/admin/products/{id}", updatedProduct.id())
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(productUpdateRequest.id()))
                    .andExpect(jsonPath("$.name").value(productUpdateRequest.name()));
        }

        @Test
        void updateProduct_ShouldReturnBadRequest_WhenIdMismatch() throws Exception {
            ProductUpdateRequest productUpdateRequest = Instancio.of(ProductUpdateRequest.class)
                    .set(field(ProductUpdateRequest.class, "image"), Base64.getEncoder().encodeToString("imagen actualizada".getBytes()))
                    .withSeed(50)
                    .create();

            String requestBody = objectMapper.writeValueAsString(productUpdateRequest);

            mockMvc.perform(put("/admin/products/{id}", productUpdateRequest.id() + 1)
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteProductByIdTests {
        @Test
        void deleteProductById_ShouldReturnNoContent_WhenProductExists()  throws Exception {
            doNothing().when(productService).deleteById(productDto1.id());

            mockMvc.perform(delete("/admin/products/{id}", productDto1.id())
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteProductById_ShouldReturnNotFound_WhenProductDoesNotExist()  throws Exception {
            doThrow(new ResourceNotFoundException("Product not found")).when(productService).deleteById(999L);

            mockMvc.perform(delete("/admin/products/{id}", 999L)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isNotFound());
        }
    }
}