package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import java.util.List;
import java.util.Optional;

import com.tagme.tagme_store_back.persistence.dao.jpa.CategoryDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CategoryJpaDaoImpl implements CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<CategoryJpaEntity> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<CategoryJpaEntity> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public CategoryJpaEntity insert(CategoryJpaEntity categoryJpaEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public CategoryJpaEntity update(CategoryJpaEntity categoryJpaEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Long count() {
        return 0L;
    }
}
