package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.Category;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.Product;
import com.tagme.tagme_store_back.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto1;
    private ProductDto productDto2;

    private Product product1;
    private Product product2;


    @BeforeEach
    void setUp() {
        CategoryDto categoryDto = new CategoryDto(1L, "Category 1");
        Category category = new Category(1L, "Category 1");

        productDto1 = new ProductDto(1L, "Product 1", "Description 1", new BigDecimal("100.00"), new BigDecimal("0"), null, categoryDto);
        productDto2 = new ProductDto(2L, "Product 2", "Description 2", new BigDecimal("200.00"), new BigDecimal("0"), null, categoryDto);
        product1 = new Product(1L, "Product 1", "Description 1", new BigDecimal("100.00"), new BigDecimal("0"), null, category);
        product2 = new Product(2L, "Product 2", "Description 2", new BigDecimal("200.00"), new BigDecimal("0"), null, category);
    }

    @Nested
    class GetAllTests {
        @Test
        void getAll_ShouldReturnProductsPage() {
            when(productRepository.findAll(1, 10)).thenReturn(List.of(product1, product2));
            when(productRepository.getTotalProducts()).thenReturn(2L);

            Page<ProductDto> page = productService.getAll(1, 10);

            assertAll(
                    () -> assertEquals(2, page.data().size()),
                    () -> assertEquals(productDto1.id(), page.data().getFirst().id()),
                    () -> assertEquals(productDto2.id(), page.data().getLast().id())
            );
        }

        @Test
        void getAll_ShouldReturnEmptyPage_WhenNoProductsFound() {
            when(productRepository.findAll(1, 10)).thenReturn(List.of());
            when(productRepository.getTotalProducts()).thenReturn(0L);

            Page<ProductDto> page = productService.getAll(1, 10);

            assertTrue(page.data().isEmpty());
        }
    }

    @Test
    void getTotalProducts_ShouldReturnCorrectCount() {
        when(productRepository.getTotalProducts()).thenReturn(5L);

        long totalProducts = productService.getTotalProducts();

        assertEquals(5L, totalProducts);
    }

    @Nested
    class GetByIdTests {
        @Test
        void getById_ShouldReturnProduct_WhenProductExists() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

            ProductDto result = productService.getById(1L);

            assertEquals(productDto1.id(), result.id());
        }

        @Test
        void getById_ShouldThrowException_WhenProductDoesNotExist() {
            when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productService.getById(1L));
        }
    }

    @Nested
    class GetProductsByCategoryIdTests {
        @Test
        void getProductsByCategoryId_ShouldReturnProducts_WhenProductsExist() {
            when(productRepository.findProductsByCategoryId(1L)).thenReturn(List.of(product1, product2));

            List<ProductDto> results = productService.getProductsByCategoryId(1L);

            assertAll(
                    () -> assertEquals(2, results.size()),
                    () -> assertEquals(productDto1.id(), results.getFirst().id()),
                    () -> assertEquals(productDto2.id(), results.getLast().id())
            );
        }

        @Test
        void getProductsByCategoryId_ShouldReturnEmptyList_WhenNoProductsFound() {
            when(productRepository.findProductsByCategoryId(1L)).thenReturn(List.of());

            List<ProductDto> results = productService.getProductsByCategoryId(1L);

            assertTrue(results.isEmpty());
        }
    }

    @Nested
    class DeleteByIdTests {
        @Test
        void deleteById_ShouldDeleteProduct_WhenProductExists() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

            productService.deleteById(1L);

            verify(productRepository).deleteById(1L);
        }

        @Test
        void deleteById_ShouldThrowException_WhenProductDoesNotExist() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(1L));
        }
    }

    @Test
    void create_ShouldReturnCreatedProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        ProductDto result = productService.create(productDto1);

        verify(productRepository).save(product1);
        assertEquals(productDto1.name(), result.name());
    }

    @Test
    void update_ShouldReturnUpdatedProduct() {
        when(productRepository.update(any(Product.class))).thenReturn(product1);

        ProductDto result = productService.update(productDto1);

        verify(productRepository).update(product1);
        assertEquals(productDto1.name(), result.name());
    }
}