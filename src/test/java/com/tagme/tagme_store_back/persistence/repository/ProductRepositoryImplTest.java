package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;
import com.tagme.tagme_store_back.persistence.mapper.ProductMapper;
import org.instancio.Instancio;
import org.instancio.Select;
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
class ProductRepositoryImplTest {
    @Mock
    private ProductJpaDao productDao;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private ProductJpaEntity productJpaEntity1;
    private ProductJpaEntity productJpaEntity2;

    private ProductDto productDto1;
    private ProductDto productDto2;



    @BeforeEach
    void setUp() {
        productJpaEntity1 = Instancio.of(ProductJpaEntity.class).withSeed(10).create();
        productJpaEntity2 = Instancio.of(ProductJpaEntity.class).withSeed(20).create();

        productDto1 = Instancio.of(ProductDto.class).withSeed(10).create();
        productDto2 = Instancio.of(ProductDto.class).withSeed(20).create();
    }

    @Nested
    class FindByIdTests {
        @Test
        void testFindById_ProductExists() {
            when(productDao.findById(1L)).thenReturn(Optional.of(productJpaEntity1));
            Optional<ProductDto> result = productRepository.findById(1L);
            assertTrue(result.isPresent());
            assertEquals(productDto1.id(), result.get().id());
        }

        @Test
        void testFindById_ProductDoesNotExist() {
            when(productDao.findById(1L)).thenReturn(Optional.empty());
            Optional<ProductDto> result = productRepository.findById(1L);
            assertFalse(result.isPresent());
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void testFindAll_ReturnsPaginatedProducts() {
            when(productDao.findAll(1, 2)).thenReturn(List.of(productJpaEntity1, productJpaEntity2));
            when(productDao.count()).thenReturn(2L);

            Page<ProductDto> result = productRepository.findAll(1, 2);

            assertAll(
                    () -> assertEquals(2, result.data().size()),
                    () -> assertEquals(productDto1.id(), result.data().get(0).id()),
                    () -> assertEquals(productDto2.id(), result.data().get(1).id()),
                    () -> assertEquals(1, result.pageNumber()),
                    () -> assertEquals(2, result.pageSize()),
                    () -> assertEquals(2L, result.totalElements())
            );
        }
    }

    @Nested
    class FindProductsByCategoryIdTests {
        @Test
        void testFindProductsByCategoryId_ReturnsProducts() {
            when(productDao.findProductsByCategoryId(1L)).thenReturn(List.of(productJpaEntity1, productJpaEntity2));

            List<ProductDto> result = productRepository.findProductsByCategoryId(1L);

            assertAll(
                    () -> assertEquals(2, result.size()),
                    () -> assertEquals(productDto1.id(), result.get(0).id()),
                    () -> assertEquals(productDto2.id(), result.get(1).id())
            );
        }
    }

    @Nested
    class DeleteByIdTests {
        @Test
        void testDeleteById_CallsDaoDelete() {
            doNothing().when(productDao).deleteById(1L);

            productRepository.deleteById(1L);

            verify(productDao, times(1)).deleteById(1L);
        }
    }

    @Nested
    class SaveTests {
        @Test
        void testSave_NewProduct_InsertsProduct() {
            ProductDto newProductDto = Instancio.of(ProductDto.class)
                    .withSeed(30)
                    .set(Select.field("id"), null)
                    .create();

            ProductJpaEntity savedEntity = ProductMapper.fromProductDtoToProductJpaEntity(newProductDto);
            savedEntity.setId(1L);

            when(productDao.insert(any(ProductJpaEntity.class))).thenReturn(savedEntity);

            ProductDto result = productRepository.save(newProductDto);

            assertEquals(newProductDto.name(), result.name());
            assertEquals(1L, result.id());
            verify(productDao, times(1)).insert(any(ProductJpaEntity.class));
        }

        @Test
        void testSave_ExistingProduct_UpdatesProduct() {
            ProductDto existingProductDto = Instancio.of(ProductDto.class).withSeed(10).create();
            ProductJpaEntity existingProductJpaEntity = Instancio.of(ProductJpaEntity.class).withSeed(10).create();

            when(productDao.update(any(ProductJpaEntity.class))).thenReturn(existingProductJpaEntity);

            ProductDto result = productRepository.save(existingProductDto);

            assertEquals(existingProductDto.id(), result.id());
            verify(productDao, times(1)).update(any(ProductJpaEntity.class));
        }
    }

    @Nested
    class GetTotalProductsTests {
        @Test
        void testGetTotalProducts_ReturnsCount() {
            when(productDao.count()).thenReturn(5L);

            Long result = productRepository.getTotalProducts();

            assertEquals(5L, result);
        }
    }
}