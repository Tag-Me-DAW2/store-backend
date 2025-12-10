package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import java.util.List;
import java.util.Optional;

import com.tagme.tagme_store_back.persistence.dao.jpa.CategoryJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.CategoryJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CategoryJpaDaoImpl implements CategoryJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CategoryJpaEntity> findAll() {
        String sql = "SELECT c FROM CategoryJpaEntity c";
        return entityManager.createQuery(sql, CategoryJpaEntity.class).getResultList();
    }

    @Override
    public Optional<CategoryJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CategoryJpaEntity.class, id));
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(CategoryJpaEntity.class, id));
    }

    @Override
    public CategoryJpaEntity insert(CategoryJpaEntity categoryJpaEntity) {
        entityManager.persist(categoryJpaEntity);
        return categoryJpaEntity;
    }

    @Override
    public CategoryJpaEntity update(CategoryJpaEntity categoryJpaEntity) {
        return entityManager.merge(categoryJpaEntity);
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(c) FROM CategoryJpaEntity c";
        return entityManager.createQuery(sql, Long.class).getSingleResult();
    }
}
