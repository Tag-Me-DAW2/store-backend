package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.annotation.DaoTest;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@DaoTest
class ProductJpaDaoImplTest extends BaseJpaDaoTest<ProductJpaDao> {
    @Nested
    class FindAllTests {
        @DisplayName("Find All Products with Pagination should return list of products whit given page and size")
        @Test
        void findAllTests() {
            int page = 1;
            int size = 5;
            List<ProductJpaEntity> result = dao.findAll(page, size);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(size, result.size()),
                    () -> assertEquals(1L, result.getFirst().getId()),
                    () -> assertEquals(5L, result.getLast().getId())
            );
        }
    }

    @Nested
    class FindByIdTests {
        @DisplayName("Given an existing product ID, when findById is called, then the corresponding product is returned")
        @Test
        void findByExistingId() {
            Long productId = 1L;
            Optional<ProductJpaEntity> expectedProduct = dao.findById(productId);

            assertTrue(expectedProduct.isPresent());
        }

        @DisplayName("Given a non-existing product ID, when findById is called, then an empty Optional is returned")
        @Test
        void findByNonExistingId() {
            Long productId = 999L;
            Optional<ProductJpaEntity> product = dao.findById(productId);
            assertTrue(product.isEmpty());
        }
    }

    @Nested
    class FindProductsByCategoryIdTests {
        @DisplayName("Given an existing category ID, when findProductsByCategoryId is called, then the corresponding products are returned")
        @Test
        void findByExistingCategoryId() {
            Long categoryId = 1L;
            List<ProductJpaEntity> products = dao.findProductsByCategoryId(categoryId);

            assertAll(
                    () -> assertNotNull(products),
                    () -> assertFalse(products.isEmpty()),
                    () -> assertTrue(products.stream().allMatch(p -> p.getCategory().getId().equals(categoryId)))
            );
        }

        @DisplayName("Given a non-existing category ID, when findProductsByCategoryId is called, then an empty list is returned")
        @Test
        void findByNonExistingCategoryId() {
            Long categoryId = 999L;
            List<ProductJpaEntity> products = dao.findProductsByCategoryId(categoryId);

            assertAll(
                    () -> assertNotNull(products),
                    () -> assertTrue(products.isEmpty())
            );
        }
    }

    @Nested
    class DeleteByIdTests {
        @DisplayName("Given an existing product ID, when deleteById is called, then the product is deleted")
        @Test
        void deleteByExistingId() {
            Long productId = 1L;
            dao.deleteById(productId);

            assertTrue(dao.findById(productId).isEmpty());
        }
    }

    @Nested
    class InsertTests {
        @DisplayName("Given a new product entity, when insert is called, then the product is persisted and returned")
        @Test
        void insertNewProduct() {
            ProductJpaEntity newProduct = Instancio.of(ProductJpaEntity.class)
                    .set(field(ProductJpaEntity::getId), null)
                    .set(field(ProductJpaEntity::getCategory), Instancio.of(CategoryJpaEntity.class).set(field(CategoryJpaEntity::getId), 1L).create())
                    .set(field(ProductJpaEntity::getDiscountPercentage), new BigDecimal("10"))
                    .set(field(ProductJpaEntity::getCreatedAt), null)
                    .create();

            ProductJpaEntity insertedProduct = dao.insert(newProduct);

            assertAll(
                    () -> assertNotNull(insertedProduct),
                    () -> assertNotNull(insertedProduct.getId()),
                    () -> assertEquals(newProduct.getName(), insertedProduct.getName())
            );
        }
    }

    @Nested
    class UpdateTests {
        @DisplayName("Update existing product should be reflected")
        @Test
        void updateExistingProduct() {
            ProductJpaEntity existingProduct = dao.findById(1L).get();
            existingProduct.setName("Updated product");

            dao.update(existingProduct);

            assertEquals("Updated product", dao.findById(1L).get().getName());
        }

        @DisplayName("Trying to update a non-existing product should throw an exception")
        @Test
        void updateNonExistingProduct() {
            ProductJpaEntity nonExistingProduct = Instancio.of(ProductJpaEntity.class)
                    .set(field(ProductJpaEntity::getId), 999L)
                    .set(field(ProductJpaEntity::getCategory), Instancio.of(CategoryJpaEntity.class).set(field(CategoryJpaEntity::getId), 1L).create())
                    .set(field(ProductJpaEntity::getDiscountPercentage), new BigDecimal("10"))
                    .set(field(ProductJpaEntity::getCreatedAt), null)
                    .create();

            assertThrows(ResourceNotFoundException.class, () -> dao.update(nonExistingProduct));
        }
    }

    @Test
    void count_ShouldReturnTotalNumberOfProducts() {
        Long expectedCount = (long) dao.findAll(1, Integer.MAX_VALUE).size();

        Long count = dao.count();

        assertEquals(expectedCount, count);
    }
}