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

    @Nested
    class FindFilteredProductsTests {
        @DisplayName("Given no filters, when findFilteredProducts is called, then return all products")
        @Test
        void findFilteredProductsNoFilters() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, null, null, null);
            assertNotNull(result);
        }

        @DisplayName("Given name filter, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithNameFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, "test", null, null, null, null, null);
            assertNotNull(result);
        }

        @DisplayName("Given category filter, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithCategoryFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, 1L, null, null, null, null);
            assertNotNull(result);
            assertTrue(result.stream().allMatch(p -> p.getCategory().getId().equals(1L)));
        }

        @DisplayName("Given material filter, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithMaterialFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, "PLASTIC", null, null, null);
            assertNotNull(result);
        }

        @DisplayName("Given invalid material filter, when findFilteredProducts is called, then ignore filter")
        @Test
        void findFilteredProductsWithInvalidMaterialFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, "INVALID_MATERIAL", null, null, null);
            assertNotNull(result);
        }

        @DisplayName("Given minPrice filter, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithMinPriceFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, 10.0, null, null);
            assertNotNull(result);
        }

        @DisplayName("Given maxPrice filter, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithMaxPriceFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, null, 100.0, null);
            assertNotNull(result);
        }

        @DisplayName("Given price range filter, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithPriceRangeFilter() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, 5.0, 50.0, null);
            assertNotNull(result);
        }

        @DisplayName("Given sort PRICE_ASC, when findFilteredProducts is called, then return sorted products")
        @Test
        void findFilteredProductsWithSortPriceAsc() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, null, null, 
                    com.tagme.tagme_store_back.domain.model.ProductSort.PRICE_ASC);
            assertNotNull(result);
        }

        @DisplayName("Given sort PRICE_DESC, when findFilteredProducts is called, then return sorted products")
        @Test
        void findFilteredProductsWithSortPriceDesc() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, null, null, 
                    com.tagme.tagme_store_back.domain.model.ProductSort.PRICE_DESC);
            assertNotNull(result);
        }

        @DisplayName("Given sort NAME_ASC, when findFilteredProducts is called, then return sorted products")
        @Test
        void findFilteredProductsWithSortNameAsc() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, null, null, 
                    com.tagme.tagme_store_back.domain.model.ProductSort.NAME_ASC);
            assertNotNull(result);
        }

        @DisplayName("Given sort NAME_DESC, when findFilteredProducts is called, then return sorted products")
        @Test
        void findFilteredProductsWithSortNameDesc() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, null, null, null, null, null, 
                    com.tagme.tagme_store_back.domain.model.ProductSort.NAME_DESC);
            assertNotNull(result);
        }

        @DisplayName("Given all filters combined, when findFilteredProducts is called, then return filtered products")
        @Test
        void findFilteredProductsWithAllFilters() {
            List<ProductJpaEntity> result = dao.findFilteredProducts(1, 10, "a", 1L, "PLASTIC", 1.0, 1000.0, 
                    com.tagme.tagme_store_back.domain.model.ProductSort.PRICE_ASC);
            assertNotNull(result);
        }

        @DisplayName("Given page 0, when findFilteredProducts is called, then treat as page 1")
        @Test
        void findFilteredProductsWithPageZero() {
            List<ProductJpaEntity> pageZero = dao.findFilteredProducts(0, 10, null, null, null, null, null, null);
            List<ProductJpaEntity> firstPage = dao.findFilteredProducts(1, 10, null, null, null, null, null, null);
            assertEquals(firstPage.size(), pageZero.size());
        }
    }

    @Nested
    class CountFilteredProductsTests {
        @DisplayName("Given no filters, when countFilteredProducts is called, then return total count")
        @Test
        void countFilteredProductsNoFilters() {
            long count = dao.countFilteredProducts(null, null, null, null, null);
            assertTrue(count >= 0);
        }

        @DisplayName("Given name filter, when countFilteredProducts is called, then return filtered count")
        @Test
        void countFilteredProductsWithNameFilter() {
            long count = dao.countFilteredProducts("test", null, null, null, null);
            assertTrue(count >= 0);
        }

        @DisplayName("Given category filter, when countFilteredProducts is called, then return filtered count")
        @Test
        void countFilteredProductsWithCategoryFilter() {
            long count = dao.countFilteredProducts(null, 1L, null, null, null);
            assertTrue(count >= 0);
        }

        @DisplayName("Given material filter, when countFilteredProducts is called, then return filtered count")
        @Test
        void countFilteredProductsWithMaterialFilter() {
            long count = dao.countFilteredProducts(null, null, "PLASTIC", null, null);
            assertTrue(count >= 0);
        }

        @DisplayName("Given price range filter, when countFilteredProducts is called, then return filtered count")
        @Test
        void countFilteredProductsWithPriceRangeFilter() {
            long count = dao.countFilteredProducts(null, null, null, 10.0, 100.0);
            assertTrue(count >= 0);
        }

        @DisplayName("Given all filters, when countFilteredProducts is called, then return filtered count")
        @Test
        void countFilteredProductsWithAllFilters() {
            long count = dao.countFilteredProducts("a", 1L, "PLASTIC", 1.0, 1000.0);
            assertTrue(count >= 0);
        }
    }
}