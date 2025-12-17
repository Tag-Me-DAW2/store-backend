package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.persistence.dao.jpa.CategoryJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;
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

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryImplTest {
    @Mock
    private CategoryJpaDao categoryDao;

    @InjectMocks
    private CategoryRepositoryImpl categoryRepositoryImpl;

    CategoryDto categoryDto1;
    CategoryDto categoryDto2;

    CategoryJpaEntity categoryJpaEntity1;
    CategoryJpaEntity categoryJpaEntity2;

    @BeforeEach
    void setUp() {
        categoryDto1 = Instancio.of(CategoryDto.class).withSeed(10).create();
        categoryDto2 = Instancio.of(CategoryDto.class).withSeed(10).create();

        categoryJpaEntity1 = Instancio.of(CategoryJpaEntity.class).withSeed(10).create();
        categoryJpaEntity2 = Instancio.of(CategoryJpaEntity.class).withSeed(10).create();
    }

    @Nested
    class FindAllTests {
        @Test
        void findAll_ShouldReturnListOfCategoryDto() {
            when(categoryDao.findAll(anyInt(), anyInt())).thenReturn(List.of(categoryJpaEntity1, categoryJpaEntity2));
            Page<CategoryDto> result = categoryRepositoryImpl.findAll(1, 10);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(categoryDto1, result.data().getFirst()),
                    () -> assertEquals(categoryDto2, result.data().getLast()),
                    () -> verify(categoryDao).findAll(1,10)
            );
        }

        @Test
        void findAll_WhenNoCategories_ShouldReturnEmptyList() {
            when(categoryDao.findAll(anyInt(), anyInt())).thenReturn(List.of());
            Page<CategoryDto> result = categoryRepositoryImpl.findAll(1, 10);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertTrue(result.data().isEmpty()),
                    () -> verify(categoryDao).findAll(1, 10)
            );
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void findById_ShouldReturnCategoryDto_WhenCategoryExists() {
            when(categoryDao.findById(categoryDto1.id())).thenReturn(Optional.of(categoryJpaEntity1));

            Optional<CategoryDto> result = categoryRepositoryImpl.findById(categoryDto1.id());

            assertAll(
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(categoryDto1, result.get()),
                    () -> verify(categoryDao).findById(categoryDto1.id())
            );
        }

        @Test
        void findById_ShouldReturnEmptyOptional_WhenCategoryDoesNotExist() {
            when(categoryDao.findById(categoryDto1.id())).thenReturn(Optional.empty());

            Optional<CategoryDto> result = categoryRepositoryImpl.findById(categoryDto1.id());

            assertAll(
                    () -> assertTrue(result.isEmpty()),
                    () -> verify(categoryDao).findById(categoryDto1.id())
            );
        }
    }

    @Test
    void deleteById_ShouldCallDaoDeleteMethod() {
        categoryRepositoryImpl.deleteById(categoryDto1.id());

        verify(categoryDao).deleteById(categoryDto1.id());
    }

    @Nested
    class SaveTests {
        @Test
        void save_ShouldInsertCategory_WhenIdIsNull() {
            CategoryDto categoryDtoWithNullId = Instancio.of(CategoryDto.class)
                    .ignore(field(CategoryDto::id))
                    .withSeed(10).create();

            when(categoryDao.insert(any())).thenReturn(categoryJpaEntity1);

            CategoryDto result = categoryRepositoryImpl.save(categoryDtoWithNullId);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(categoryDto1, result),
                    () -> verify(categoryDao).insert(any())
            );
        }

        @Test
        void save_ShouldUpdateCategory_WhenIdIsNotNull() {

            when(categoryDao.update(categoryJpaEntity1)).thenReturn(categoryJpaEntity1);

            CategoryDto result = categoryRepositoryImpl.save(categoryDto1);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(categoryDto1, result),
                    () -> verify(categoryDao).update(categoryJpaEntity1)
            );
        }
    }
}