package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.ProductSort;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getAll(int page, int size);
    ProductDto getById(Long id);
    Long getTotalProducts();
    List<ProductDto> getProductsByCategoryId(Long categoryId);
    void deleteById(Long id);
    ProductDto create(ProductDto product);
    ProductDto update(ProductDto product);
    Page<ProductDto> getFilteredProducts(int page, int size, String name, Long categoryId, String material, Double minPrice, Double maxPrice, ProductSort sort);
}
