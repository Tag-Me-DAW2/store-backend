package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.ProductMapper;
import com.tagme.tagme_store_back.domain.model.Category;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.Product;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import com.tagme.tagme_store_back.domain.repository.ProductRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    protected CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto1;
    private ProductDto productDto2;
    private ProductDto productDto3;

    private Product product1;
    private Product product2;
    private Product product3;


    @BeforeEach
    void setUp() {
        productDto1 = Instancio.of(ProductDto.class).withSeed(10).create();
        productDto2 = Instancio.of(ProductDto.class).withSeed(20).create();

        product1 = Instancio.of(Product.class).withSeed(10).create();
        product2 = Instancio.of(Product.class).withSeed(20).create();

        product3 = ProductMapper.fromProductDtoToProduct(productDto1);
        productDto3 = ProductMapper.fromProductToProductDto(product3);
    }

    @DisplayName("GetAll Should return all products paginated")
    @Test
    void getAllTest() {
        Page<ProductDto> productDtoPage = new Page<>(
                List.of(productDto1, productDto2),
                1,
                2,
                2L
        );
        when(productRepository.findAll(1,2)).thenReturn(productDtoPage);

        Page<ProductDto> result = productService.getAll(1, 2);

        assertAll(
                () -> assertEquals(2, result.data().size()),
                () -> assertEquals(productDto1.id(), result.data().get(0).id()),
                () -> assertEquals(productDto2.id(), result.data().get(1).id()),
                () -> assertEquals(1, result.pageNumber()),
                () -> assertEquals(2, result.pageSize()),
                () -> assertEquals(2L, result.totalElements())
        );
    }

    @Nested
    class findByIdTest {
        @DisplayName("When id is valid, should return product")
        @Test
        void whenIdIsValid_ShouldReturnProduct() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(productDto1));

            ProductDto result = productService.getById(1L);

            assertAll(
                    () -> assertEquals(productDto1.id(), result.id()),
                    () -> assertEquals(productDto1.name(), result.name())
            );
        }

        @DisplayName("When id is null, should throw RuntimeException")
        @Test
        void whenIdIsNull_ShouldThrowRuntimeException() {
            assertThrows(RuntimeException.class, () -> productService.getById(null));
        }

        @DisplayName("When product not found, should throw ResourceNotFoundException")
        @Test
        void whenProductNotFound_ShouldThrowResourceNotFoundException() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
        }
    }

    @Nested
    class GetProductsByCategoryIdTest {
        @DisplayName("When category id is valid, should return products list")
        @Test
        void whenCategoryIdIsValid_ShouldReturnProductsList() {
            when(productRepository.findProductsByCategoryId(1L)).thenReturn(List.of(productDto1, productDto2));
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "Electronics")));

            List<ProductDto> result = productService.getProductsByCategoryId(1L);

            assertAll(
                    () -> assertEquals(2, result.size()),
                    () -> assertEquals(productDto1.id(), result.get(0).id()),
                    () -> assertEquals(productDto2.id(), result.get(1).id())
            );
        }

        @DisplayName("When category id is null, should throw RuntimeException")
        @Test
        void whenCategoryIdIsNull_ShouldThrowRuntimeException() {
             assertThrows(RuntimeException.class, () -> productService.getProductsByCategoryId(null));
        }

        @DisplayName("When category not found, should throw ResourceNotFoundException")
        @Test
        void whenCategoryNotFound_ShouldThrowResourceNotFoundException() {
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
           assertThrows(ResourceNotFoundException.class, () -> productService.getProductsByCategoryId(99L));
        }
    }

    @Nested
    class deleteByIdTest {
        @DisplayName("When id is valid, should delete product")
        @Test
        void whenIdIsValid_ShouldDeleteProduct() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(productDto1));
            doNothing().when(productRepository).deleteById(1L);

            assertDoesNotThrow(() -> productService.deleteById(1L));
            verify(productRepository, times(1)).deleteById(1L);
        }

        @DisplayName("When id is null, should throw RuntimeException")
        @Test
        void whenIdIsNull_ShouldThrowRuntimeException() {
            assertThrows(RuntimeException.class, () -> productService.deleteById(null));
        }

        @DisplayName("When product not found, should throw ResourceNotFoundException")
        @Test
        void whenProductNotFound_ShouldThrowResourceNotFoundException() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(99L));
        }
    }

    @Nested
    class CreateProductTest {
        @DisplayName("When category exists, should create product")
        @Test
        void whenCategoryExists_ShouldCreateProduct() {
            when(categoryRepository.findById(productDto3.category().id())).thenReturn(Optional.of(new Category(productDto3.category().id(), productDto3.category().name())));
            when(productRepository.save(productDto3)).thenReturn(productDto3);

            ProductDto result = productService.create(productDto3);

            assertAll(
                    () -> assertEquals(productDto3.id(), result.id()),
                    () -> assertEquals(productDto3.name(), result.name())
            );
        }

        @DisplayName("When category does not exist, should throw ResourceNotFoundException")
        @Test
        void whenCategoryDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(categoryRepository.findById(productDto1.category().id())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productService.create(productDto1));
        }
    }

    @Nested
    class UpdateProductTest {
        @DisplayName("When product and category exist, should update product")
        @Test
        void whenProductAndCategoryExist_ShouldUpdateProduct() {
            when(productRepository.findById(productDto3.id())).thenReturn(Optional.of(productDto3));
            when(categoryRepository.findById(productDto3.category().id())).thenReturn(Optional.of(new Category(productDto3.category().id(), productDto3.category().name())));
            when(productRepository.save(productDto3)).thenReturn(productDto3);

            ProductDto result = productService.update(productDto3);

            assertAll(
                    () -> assertEquals(productDto3.id(), result.id()),
                    () -> assertEquals(productDto3.name(), result.name())
            );
        }

        @DisplayName("When product does not exist, should throw ResourceNotFoundException")
        @Test
        void whenProductDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(productRepository.findById(productDto1.id())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> productService.update(productDto1));
        }

        @DisplayName("When category does not exist, should throw ResourceNotFoundException")
        @Test
        void whenCategoryDoesNotExist_ShouldThrowResourceNotFoundException() {
            when(productRepository.findById(productDto1.id())).thenReturn(Optional.of(productDto1));
            when(categoryRepository.findById(productDto1.category().id())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> productService.update(productDto1));
        }
    }

    @DisplayName("Count Should return total number of products")
    @Test
    void countTest() {
        when(productRepository.getTotalProducts()).thenReturn(5L);

        Long result = productService.getTotalProducts();

        assertEquals(5L, result);
    }

}