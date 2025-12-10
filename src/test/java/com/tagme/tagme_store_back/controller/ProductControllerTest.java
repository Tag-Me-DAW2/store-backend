package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.webModel.request.ProductInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.ProductUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.ProductDetailResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.service.ProductService;
import com.tagme.tagme_store_back.domain.service.UserService;
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
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private ProductDto productDto1;
    private ProductDto productDto2;

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

        productDto2 = Instancio.of(ProductDto.class)
                .supply(field(ProductDto::image), () -> {
                    try {
                        byte[] bytes = "imagen de prueba 2".getBytes();
                        return new SerialBlob(bytes);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .withSeed(20)
                .create();
    }

    @Nested
    class GetAllProductsTests {
        @Test
        void getAllProducts_ShouldReturnProductsWithDefaultPagination_WhenNoPaginationParamsProvided() throws Exception {
            when(productService.getAll(1, 10)).thenReturn(new Page<>(List.of(productDto1, productDto2), 1, 10, 2L, 2));

            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].id").value(productDto1.id()))
                    .andExpect(jsonPath("$.data[1].id").value(productDto2.id()))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10))
                    .andExpect(jsonPath("$.totalElements").value(2L))
                    .andExpect(jsonPath("$.totalPages").value(2));
        }

        @Test
        void getAllProducts_ShouldReturnProductsWithSpecifiedPagination_WhenPaginationParamsProvided() throws Exception {
            when(productService.getAll(2, 5)).thenReturn(new Page<>(List.of(productDto1), 2, 5, 5L, 2));

            mockMvc.perform(get("/products")
                            .param("page", "2")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.data[0].id").value(productDto1.id()))
                    .andExpect(jsonPath("$.pageNumber").value(2))
                    .andExpect(jsonPath("$.pageSize").value(5))
                    .andExpect(jsonPath("$.totalElements").value(5L))
                    .andExpect(jsonPath("$.totalPages").value(2));
        }
    }

    @Nested
    class GetProductByIdTests {
        @Test
        void getProductById_ShouldReturnProduct_WhenProductExists() throws Exception {
            when(productService.getById(productDto1.id())).thenReturn(productDto1);

            mockMvc.perform(get("/products/{id}", productDto1.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(productDto1.id()))
                    .andExpect(jsonPath("$.name").value(productDto1.name()));
        }

        @Test
        void getProductById_ShouldReturnNotFound_WhenProductDoesNotExist() throws Exception {
            when(productService.getById(999L)).thenThrow(new ResourceNotFoundException("Product not found"));
            mockMvc.perform(get("/products/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateProductTests {
        @Test
        void createProduct_ShouldReturnCreatedProduct_WhenValidRequest() throws Exception {
            ProductDto createdProduct = Instancio.of(ProductDto.class).withSeed(30).create();
            when(productService.create(any(ProductDto.class))).thenReturn(createdProduct);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(
                    Instancio.of(ProductDetailResponse.class).withSeed(30)
                            .set(field(ProductDetailResponse.class, "image"), Base64.getEncoder().encodeToString("imagen prueba".getBytes()))
                            .create());

            mockMvc.perform(post("/products")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(createdProduct.id()))
                    .andExpect(jsonPath("$.name").value(createdProduct.name()));
        }

        @Test
        void createProduct_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(Instancio.of(ProductInsertRequest.class)
                    .set(field(ProductInsertRequest.class, "name"), null)
                    .withSeed(100)
                    .create());

            mockMvc.perform(post("/products")
                            .contentType("application/json")
                            .content(requestBody))
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

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(productUpdateRequest);

            System.out.println(productUpdateRequest+ " ");
            System.out.println(requestBody+ "sadf");
            mockMvc.perform(put("/products/{id}", updatedProduct.id())
                            .contentType("application/json")
                            .content(requestBody))
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

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(productUpdateRequest);

            mockMvc.perform(put("/products/{id}", productUpdateRequest.id() + 1)
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteProductByIdTests {
        @Test
        void deleteProductById_ShouldReturnNoContent_WhenProductExists()  throws Exception {
            doNothing().when(productService).deleteById(productDto1.id());

            mockMvc.perform(delete("/products/{id}", productDto1.id()))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteProductById_ShouldReturnNotFound_WhenProductDoesNotExist()  throws Exception {
            doThrow(new ResourceNotFoundException("Product not found")).when(productService).deleteById(999L);

            mockMvc.perform(delete("/products/{id}", 999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetTotalProductsTests {
        @Test
        void getTotalProducts_ShouldReturnTotalCount() throws Exception {
            when(productService.getTotalProducts()).thenReturn(42L);

            mockMvc.perform(get("/products/total"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("42"));
        }
    }
}