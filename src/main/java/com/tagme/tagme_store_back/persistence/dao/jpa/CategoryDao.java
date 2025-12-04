package com.tagme.tagme_store_back.persistence.dao.jpa;

import java.util.List;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;

public interface CategoryDao extends  GenericDao<CategoryJpaEntity> {
    List<CategoryJpaEntity> findAll();
}
