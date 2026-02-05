package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.ProductSort;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Page<ProductDto> findAll(int page, int size);
    Optional<ProductDto> findById(Long id);
    List<ProductDto> findProductsByCategoryId(Long categoryId);
    void deleteById(Long id);
    ProductDto save(ProductDto productDto);
    Long getTotalProducts();
    Page<ProductDto> findFilteredProducts(int page, int size, String name, Long categoryId, String material, Double minPrice, Double maxPrice, ProductSort sort);
}
