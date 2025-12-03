package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Page;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getAll(int page, int size);
    long getTotalProducts();
    ProductDto getById(Long id);
    List<ProductDto> getProductsByCategoryId(Long categoryId);
    void deleteById(Long id);
    ProductDto create(ProductDto product);
    ProductDto update(ProductDto product);
}
