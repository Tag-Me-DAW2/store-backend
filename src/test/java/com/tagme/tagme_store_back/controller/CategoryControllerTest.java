package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.mapper.CategoryMapper;
import com.tagme.tagme_store_back.controller.webModel.request.CategoryRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CategoryResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.CategoryService;
import com.tagme.tagme_store_back.domain.service.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;

    @BeforeEach
    void setUp() {
        categoryDto1 = Instancio.of(CategoryDto.class).withSeed(10).create();
        categoryDto2 = Instancio.of(CategoryDto.class).withSeed(20).create();
    }

    @Nested
    class GetAllCategoriesTests {
        @Test
        void getAllCategories_ShouldReturnCategories_WhenCategoriesExist() throws Exception {
            List<CategoryDto> categories = List.of(categoryDto1, categoryDto2);
            when(categoryService.getAll()).thenReturn(categories);

            mockMvc.perform(get("/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(categories.size()))
                    .andExpect(jsonPath("$[0].id").value(categories.getFirst().id()))
                    .andExpect(jsonPath("$[0].name").value(categories.getFirst().name()))
                    .andExpect(jsonPath("$[1].id").value(categories.getLast().id()))
                    .andExpect(jsonPath("$[1].name").value(categories.getLast().name()));
        }

        @Test
        void getAllCategories_ShouldReturnEmptyList_WhenNoCategoriesExist() throws Exception {
            when(categoryService.getAll()).thenReturn(List.of());

            mockMvc.perform(get("/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    class GetCategoryByIdTests {
        @Test
        void getCategoryById_ShouldReturnCategory_WhenCategoryExists() throws Exception {
            when(categoryService.getById(categoryDto1.id())).thenReturn(categoryDto1);

            mockMvc.perform(get("/categories/{id}", categoryDto1.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(categoryDto1.id()))
                    .andExpect(jsonPath("$.name").value(categoryDto1.name()));
        }

        @Test
        void getCategoryById_ShouldReturnNotFound_WhenCategoryDoesNotExist() throws Exception {
            Long nonExistentId = 999L;
            when(categoryService.getById(nonExistentId)).thenThrow(new ResourceNotFoundException("Category not found"));

            mockMvc.perform(get("/categories/{id}", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteCategoryByIdTests {
        @Test
        void deleteCategoryById_ShouldReturnNoContent_WhenCategoryExists() throws Exception {
            doNothing().when(categoryService).deleteById(categoryDto1.id());

            mockMvc.perform(delete("/categories/{id}", categoryDto1.id()))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteCategoryById_ShouldReturnNotFound_WhenCategoryDoesNotExist() throws Exception {
            Long nonExistentId = 999L;
            doThrow(new ResourceNotFoundException("Category not found")).when(categoryService).deleteById(nonExistentId);

            mockMvc.perform(delete("/categories/{id}", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateCategoryTests {
        @Test
        void createCategory_ShouldReturnCreatedCategory_WhenValidInput() throws Exception {
            when(categoryService.create(any(CategoryDto.class))).thenReturn(categoryDto1);
            CategoryRequest categoryRequest = Instancio.of(CategoryRequest.class).withSeed(10).create();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(categoryRequest);

            mockMvc.perform(post("/categories")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(categoryDto1.id()))
                    .andExpect(jsonPath("$.name").value(categoryDto1.name()));
        }

        @Test
        void createCategory_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(new CategoryRequest(null, ""));

            mockMvc.perform(post("/categories")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateCategoryTests {
        @Test
        void updateCategory_ShouldReturnUpdatedCategory_WhenValidInput() throws Exception {
            when(categoryService.update(any(CategoryDto.class))).thenReturn(categoryDto1);
            CategoryRequest categoryRequest = Instancio.of(CategoryRequest.class).withSeed(10).create();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(categoryRequest);

            mockMvc.perform(put("/categories/{id}", categoryDto1.id())
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(categoryDto1.id()))
                    .andExpect(jsonPath("$.name").value(categoryDto1.name()));
        }

        @Test
        void updateCategory_ShouldReturnBadRequest_WhenIdMismatch() throws Exception {
            CategoryRequest categoryRequest = Instancio.of(CategoryRequest.class).withSeed(10).create();

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(categoryRequest);

            mockMvc.perform(put("/categories/{id}", categoryDto1.id() + 1)
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }
}