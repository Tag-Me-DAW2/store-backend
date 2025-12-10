package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.annotation.DaoTest;
import com.tagme.tagme_store_back.persistence.dao.jpa.CategoryJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@DaoTest
class CategoryJpaDaoImplTest extends BaseJpaDaoTest<CategoryJpaDao> {
    @Nested
    class FindAllTests {
        @Test
        void findAll_ShouldReturnAllCategories() {
            Long total = dao.count();
            List<CategoryJpaEntity> categories = dao.findAll();

            assertAll(
                    () -> assertFalse(categories.isEmpty()),
                    () -> assertEquals(total, categories.size())
            );
        }

        @Test
        @Sql(statements = "DELETE FROM tb_categories")
        void findAll_ShouldReturnEmptyList_WhenNoCategoriesExist() {
            List<CategoryJpaEntity> categories = dao.findAll();

            assertTrue(categories.isEmpty());
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void findById_ShouldReturnCategory_WhenCategoryExists() {
            Long categoryId = 1L;

            Optional<CategoryJpaEntity> category = dao.findById(categoryId);

            assertTrue(category.isPresent());
            assertEquals(categoryId, category.get().getId());
        }

        @Test
        void findById_ShouldReturnEmptyOptional_WhenCategoryDoesNotExist() {
            Long categoryId = 999L;

            Optional<CategoryJpaEntity> category = dao.findById(categoryId);

            assertTrue(category.isEmpty());
        }
    }

    @Nested
    class DeleteByIdTests {
        @Test
        void deleteById_ShouldRemoveCategory_WhenCategoryExists() {
            Long categoryId = 1L;
            Optional<CategoryJpaEntity> categoryBeforeDelete = dao.findById(categoryId);
            dao.deleteById(categoryId);
            Optional<CategoryJpaEntity> categoryAfterDelete = dao.findById(categoryId);

            assertAll(
                    () -> assertTrue(categoryBeforeDelete.isPresent()),
                    () -> assertTrue(categoryAfterDelete.isEmpty())
            );
        }
    }

    @Nested
    class InsertTests {
        @Test
        void insert_ShouldPersistAndReturnCategory() {
            CategoryJpaEntity newCategory = Instancio.of(CategoryJpaEntity.class)
                    .set(field(CategoryJpaEntity::getCreatedAt), null)
                    .ignore(field(CategoryJpaEntity.class, "id"))
                    .create();

            CategoryJpaEntity insertedCategory = dao.insert(newCategory);
            Optional<CategoryJpaEntity> fetchedCategory = dao.findById(insertedCategory.getId());

            assertAll(
                    () -> assertNotNull(insertedCategory.getId()),
                    () -> assertTrue(fetchedCategory.isPresent()),
                    () -> assertEquals(newCategory.getName(), fetchedCategory.get().getName())
            );
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void update_ShouldModifyAndReturnCategory() {
            String newName = "newName";

            Long categoryId = 1L;
            Optional<CategoryJpaEntity> category = dao.findById(categoryId);
            category.get().setName("newName");

            CategoryJpaEntity updatedCategory = dao.update(category.get());
            Optional<CategoryJpaEntity> fetchedCategory = dao.findById(categoryId);

            assertAll(
                    () -> assertEquals(newName, updatedCategory.getName()),
                    () -> assertTrue(fetchedCategory.isPresent())
            );
        }
    }

    @Test
    void count_ShouldReturnTotalNumberOfCategories() {
        Long expectedCount = (long) dao.findAll().size();

        Long actualCount = dao.count();

        assertEquals(expectedCount, actualCount);
    }
}