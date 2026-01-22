package com.tagme.tagme_store_back.persistence.dao.jpa;

import java.util.List;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;

public interface ProductJpaDao extends GenericJpaDao<ProductJpaEntity> {
    List<ProductJpaEntity> findProductsByCategoryId(Long categoryId);
    List<ProductJpaEntity> findFilteredProducts(int page, int size, String name, Long categoryId, String material, Double minPrice, Double maxPrice);
    long countFilteredProducts(String name, Long categoryId, String material, Double minPrice, Double maxPrice);
}
