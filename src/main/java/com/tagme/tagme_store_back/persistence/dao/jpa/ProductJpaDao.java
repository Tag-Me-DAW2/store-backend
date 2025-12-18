package com.tagme.tagme_store_back.persistence.dao.jpa;

import java.util.List;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;

public interface ProductJpaDao extends GenericJpaDao<ProductJpaEntity> {
    List<ProductJpaEntity> findProductsByCategoryId(Long categoryId);
}
