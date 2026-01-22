package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.ProductMaterial;
import com.tagme.tagme_store_back.domain.model.ProductSort;
import com.tagme.tagme_store_back.persistence.dao.jpa.ProductJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.ProductJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

public class ProductJpaDaoImpl implements ProductJpaDao {

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
    public List<ProductJpaEntity> findFilteredProducts(int page, int size, String name, Long categoryId, String material, Double minPrice, Double maxPrice, ProductSort sort) {
        int pageIndex = Math.max(page - 1, 0);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductJpaEntity> cq = cb.createQuery(ProductJpaEntity.class);
        Root<ProductJpaEntity> p = cq.from(ProductJpaEntity.class);

        Map querySetters = buildPredicates(cb, p, name, categoryId, material, minPrice, maxPrice, sort);
        List<Predicate> predicates = (List<Predicate>) querySetters.keySet().iterator().next();
        List<Order> orders = (List<Order>) querySetters.values().iterator().next();

        cq.where(predicates.toArray(new Predicate[0]));
        if (!orders.isEmpty()) {
            cq.orderBy(orders);
        }

        TypedQuery<ProductJpaEntity> query = entityManager.createQuery(cq)
                .setFirstResult(pageIndex * size)
                .setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countFilteredProducts(String name, Long categoryId, String material, Double minPrice, Double maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProductJpaEntity> p = cq.from(ProductJpaEntity.class);

        List<Predicate> predicates = buildPredicates(cb, p, name, categoryId, material, minPrice, maxPrice, null).keySet().iterator().next();

        cq.select(cb.count(p)).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getSingleResult();
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

    private Map<List<Predicate>, List<Order>> buildPredicates(CriteriaBuilder cb, Root<ProductJpaEntity> p, String name, Long categoryId, String material, Double minPrice, Double maxPrice, ProductSort sort) {
        List<Predicate> predicates = new java.util.ArrayList<>();
        List<Order> orders = new java.util.ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(p.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (categoryId != null) {
            predicates.add(cb.equal(p.get("category").get("id"), categoryId));
        }

        if (material != null && !material.isEmpty()) {
            try {
                ProductMaterial materialEnum = ProductMaterial.valueOf(material.toUpperCase());
                predicates.add(cb.equal(p.get("material"), materialEnum));
            } catch (IllegalArgumentException e) {
                // Invalid material value, ignore the filter
            }
        }

        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(p.get("basePrice"), BigDecimal.valueOf(minPrice)));
        }

        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(p.get("basePrice"), BigDecimal.valueOf(maxPrice)));
        }

        if (sort != null) {
            switch (sort) {
                case PRICE_ASC -> orders.add(cb.asc(p.get("basePrice")));
                case PRICE_DESC -> orders.add(cb.desc(p.get("basePrice")));
                case NAME_ASC -> orders.add(cb.asc(p.get("name")));
                case NAME_DESC -> orders.add(cb.desc(p.get("name")));
            }
        }

        return Map.of(predicates, orders);
    }
}
