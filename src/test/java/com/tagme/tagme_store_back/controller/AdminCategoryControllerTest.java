package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.webModel.request.CategoryRequest;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.UserRole;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.CategoryService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCategoryController.class)
class AdminCategoryControllerTest {
    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDto categoryDto1;

    @BeforeEach
    void setUp() {
        categoryDto1 = Instancio.of(CategoryDto.class).withSeed(10).create();
        UserDto adminUser = Instancio.of(UserDto.class)
                .set(field(UserDto.class, "role"), UserRole.ADMIN)
                .create();
        when(authService.getByToken(anyString())).thenReturn(adminUser);
    }

    @Nested
    class DeleteCategoryByIdTests {
        @Test
        void deleteCategoryById_ShouldReturnNoContent_WhenCategoryExists() throws Exception {
            doNothing().when(categoryService).deleteById(categoryDto1.id());

            mockMvc.perform(delete("/admin/categories/{id}", categoryDto1.id())
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteCategoryById_ShouldReturnNotFound_WhenCategoryDoesNotExist() throws Exception {
            Long nonExistentId = 999L;
            doThrow(new ResourceNotFoundException("Category not found")).when(categoryService).deleteById(nonExistentId);

            mockMvc.perform(delete("/admin/categories/{id}", nonExistentId)
                        .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateCategoryTests {
        @Test
        void createCategory_ShouldReturnCreatedCategory_WhenValidInput() throws Exception {
            when(categoryService.create(any(CategoryDto.class))).thenReturn(categoryDto1);
            CategoryRequest categoryRequest = Instancio.of(CategoryRequest.class).withSeed(10).create();

            String requestBody = objectMapper.writeValueAsString(categoryRequest);

            mockMvc.perform(post("/admin/categories")
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(categoryDto1.id()))
                    .andExpect(jsonPath("$.name").value(categoryDto1.name()));
        }

        @Test
        void createCategory_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(new CategoryRequest(null, ""));

            mockMvc.perform(post("/admin/categories")
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateCategoryTests {
        @Test
        void updateCategory_ShouldReturnUpdatedCategory_WhenValidInput() throws Exception {
            when(categoryService.update(any(CategoryDto.class))).thenReturn(categoryDto1);
            CategoryRequest categoryRequest = Instancio.of(CategoryRequest.class).withSeed(10).create();

            String requestBody = objectMapper.writeValueAsString(categoryRequest);

            mockMvc.perform(put("/admin/categories/{id}", categoryDto1.id())
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(categoryDto1.id()))
                    .andExpect(jsonPath("$.name").value(categoryDto1.name()));
        }

        @Test
        void updateCategory_ShouldReturnBadRequest_WhenIdMismatch() throws Exception {
            CategoryRequest categoryRequest = Instancio.of(CategoryRequest.class).withSeed(10).create();

            String requestBody = objectMapper.writeValueAsString(categoryRequest);

            mockMvc.perform(put("/admin/categories/{id}", categoryDto1.id() + 1)
                            .contentType("application/json")
                            .content(requestBody)
                            .header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isBadRequest());
        }
    }
}