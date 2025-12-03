package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll(int page, int size);
    long getTotalProducts();
    Optional<Product> findById(Long id);
    List<Product> findProductsByCategoryId(Long categoryId);
    void deleteById(Long id);
    Product save(Product product);
    Product update(Product product);
}
