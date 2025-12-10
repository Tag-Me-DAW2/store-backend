package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.Category;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    CategoryDto categoryDto1;
    CategoryDto categoryDto2;

    Category category1;
    Category category2;

    @BeforeEach
    void setUp() {
        categoryDto1 = Instancio.of(CategoryDto.class).withSeed(10).create();
        categoryDto2 = Instancio.of(CategoryDto.class).withSeed(10).create();

        category1 = Instancio.of(Category.class).withSeed(10).create();
        category2 = Instancio.of(Category.class).withSeed(10).create();
    }

    @Nested
    class GetAllTests {
        @Test
        void getAll_ShouldReturnListOfCategoryDtos() {
            when(categoryRepository.findAll()).thenReturn(List.of(categoryDto1, categoryDto2));

            List<CategoryDto> result = categoryService.getAll();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(2, result.size()),
                    () -> verify(categoryRepository).findAll(),
                    () -> assertEquals(categoryDto1, result.getFirst()),
                    () -> assertEquals(categoryDto2, result.getLast())
            );
        }

        @Test
        void getAll_ShouldReturnEmptyList_WhenNoCategoriesExist() {
            when(categoryRepository.findAll()).thenReturn(List.of());

            List<CategoryDto> result = categoryService.getAll();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertTrue(result.isEmpty()),
                    () -> verify(categoryRepository).findAll()
            );
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void getById_ShouldReturnCategoryDto_WhenCategoryExists() {
            Long categoryId = 1L;
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryDto1));

            CategoryDto result = categoryService.getById(categoryId);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(categoryDto1, result),
                    () -> verify(categoryRepository).findById(categoryId)
            );
        }

        @Test
        void getById_ShouldThrowException_WhenCategoryDoesNotExist() {
            when(categoryRepository.findById(categoryDto1.id())).thenReturn(Optional.empty());

            assertAll(
                    () -> assertThrows(ResourceNotFoundException.class, () -> categoryService.getById(categoryDto1.id())),
                    () -> verify(categoryRepository).findById(categoryDto1.id())
            );
        }
    }

    @Nested
    class DeleteByIdTests {
        @Test
        void deleteById_ShouldCallRepositoryDeleteMethod_WhenCategoryExists() {
            when(categoryRepository.findById(categoryDto1.id())).thenReturn(Optional.of(categoryDto1));

            categoryService.deleteById(categoryDto1.id());

            verify(categoryRepository).deleteById(categoryDto1.id());
        }

        @Test
        void deleteById_ShouldThrowException_WhenCategoryDoesNotExist() {
            when(categoryRepository.findById(categoryDto1.id())).thenReturn(Optional.empty());

            assertAll(
                    () -> assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteById(categoryDto1.id())),
                    () -> verify(categoryRepository).findById(categoryDto1.id())
            );
        }
    }

    @Test
    void create_ShouldSaveAndReturnCategoryDto() {
        when(categoryRepository.save(categoryDto1)).thenReturn(categoryDto1);

        CategoryDto result = categoryService.create(categoryDto1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(categoryDto1, result),
                () -> verify(categoryRepository).save(categoryDto1)
        );
    }

    @Nested
    class UpdateByIdTests {
        @Test
        void update_ShouldUpdateAndReturnCategoryDto_WhenCategoryExists() {
            when(categoryRepository.findById(categoryDto1.id())).thenReturn(Optional.of(categoryDto1));

            when(categoryRepository.save(categoryDto1)).thenReturn(categoryDto1);
            CategoryDto result = categoryService.update(categoryDto1);
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(categoryDto1, result),
                    () -> verify(categoryRepository).findById(categoryDto1.id()),
                    () -> verify(categoryRepository).save(categoryDto1)
            );
        }

        @Test
        void update_ShouldThrowException_WhenCategoryDoesNotExist() {
            when(categoryRepository.findById(categoryDto1.id())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> categoryService.update(categoryDto1));
        }
    }
}