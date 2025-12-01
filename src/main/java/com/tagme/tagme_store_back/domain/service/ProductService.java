package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.model.Page;

public interface ProductService {
    Page<ProductDto> getAll(int page, int size);
    ProductDto getById(Long id);
    ProductDto getByCategoryId(Long categoryId);
    void deleteById(Long id);
    ProductDto create(ProductDto product);
    ProductDto update(ProductDto product);
}
