package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import java.util.List;
import java.util.Optional;

import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductJpaEntity> findAll(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);

        String sql = "SELECT p FROM ProductJpaEntity p ORDER BY p.id";
        TypedQuery<ProductJpaEntity> query = entityManager
                .createQuery(sql, ProductJpaEntity.class)
                .setFirstResult(pageIndex * size)
                .setMaxResults(size);

        return query.getResultList();
    }


    @Override
    public Optional<ProductJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(ProductJpaEntity.class, id));
    }

    @Override
    public List<ProductJpaEntity> findProductsByCategoryId(Long categoryId) {
        String sql = "SELECT p FROM ProductJpaEntity p WHERE p.category.id = :categoryId";

        return entityManager.createQuery(sql, ProductJpaEntity.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(ProductJpaEntity.class, id));
    }

    @Override
    public ProductJpaEntity insert(ProductJpaEntity productJpaEntity) {
        entityManager.persist(productJpaEntity);
        return productJpaEntity;
    }

    @Override
    public ProductJpaEntity update(ProductJpaEntity productJpaEntity) {
        ProductJpaEntity managedEntity = entityManager.find(ProductJpaEntity.class, productJpaEntity.getId());
        if (managedEntity == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        return entityManager.merge(productJpaEntity);
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM ProductJpaEntity u", Long.class).getSingleResult();
    }
}
