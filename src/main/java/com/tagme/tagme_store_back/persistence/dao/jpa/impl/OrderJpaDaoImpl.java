package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class OrderJpaDaoImpl implements OrderJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderJpaEntity> findAll(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);
        int validSize = Math.max(size, 1);

        String sql = "SELECT o FROM OrderJpaEntity o ORDER BY o.id";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setFirstResult(pageIndex * validSize)
                .setMaxResults(validSize);

        return query.getResultList();
    }

    @Override
    public Optional<OrderJpaEntity> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(OrderJpaEntity.class, id));
    }

    @Override
    public OrderJpaEntity insert(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            throw new IllegalArgumentException("Order entity cannot be null");
        }
        if (orderJpaEntity.getUser() == null) {
            throw new IllegalArgumentException("Order must have a user");
        }
        if (orderJpaEntity.getOrderStatus() == null) {
            throw new IllegalArgumentException("Order must have a status");
        }

        entityManager.persist(orderJpaEntity);
        return orderJpaEntity;
    }

    @Override
    public OrderJpaEntity update(OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity == null) {
            throw new IllegalArgumentException("Order entity cannot be null");
        }
        if (orderJpaEntity.getId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null for update");
        }

        // Verificar que existe
        OrderJpaEntity existing = entityManager.find(OrderJpaEntity.class, orderJpaEntity.getId());
        if (existing == null) {
            throw new ResourceNotFoundException("Order not found with id: " + orderJpaEntity.getId());
        }

        return entityManager.merge(orderJpaEntity);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        OrderJpaEntity entity = entityManager.find(OrderJpaEntity.class, id);
        if (entity == null) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        entityManager.remove(entity);
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(o) FROM OrderJpaEntity o";
        return entityManager.createQuery(sql, Long.class).getSingleResult();
    }

    @Override
    public List<OrderJpaEntity> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        String sql = "SELECT o FROM OrderJpaEntity o WHERE o.user.id = :userId ORDER BY o.createdAt DESC";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public Optional<OrderJpaEntity> findActiveOrderByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        String sql = "SELECT o FROM OrderJpaEntity o WHERE o.user.id = :userId AND o.orderStatus = :status";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setParameter("userId", userId)
                .setParameter("status", OrderStatus.PENDING);

        List<OrderJpaEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public Optional<OrderStatus> getOrderStatus(Long orderId) {
        if (orderId == null) {
            return Optional.empty();
        }

        String sql = "SELECT o.orderStatus FROM OrderJpaEntity o WHERE o.id = :orderId";
        TypedQuery<OrderStatus> query = entityManager
                .createQuery(sql, OrderStatus.class)
                .setParameter("orderId", orderId);

        List<OrderStatus> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }
}

