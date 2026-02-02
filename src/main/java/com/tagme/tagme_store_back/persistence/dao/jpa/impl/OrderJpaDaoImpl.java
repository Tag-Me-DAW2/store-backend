package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

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

        String sql = "SELECT o FROM OrderJpaEntity o ORDER BY o.id";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setFirstResult(pageIndex * size)
                .setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Optional<OrderJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(OrderJpaEntity.class, id));
    }

    @Override
    public OrderJpaEntity insert(OrderJpaEntity orderJpaEntity) {
        entityManager.persist(orderJpaEntity);
        return orderJpaEntity;
    }

    @Override
    public OrderJpaEntity update(OrderJpaEntity orderJpaEntity) {
        return entityManager.merge(orderJpaEntity);
    }

    @Override
    public void deleteById(Long id) {
        OrderJpaEntity entity = entityManager.find(OrderJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(o) FROM OrderJpaEntity o";
        return entityManager.createQuery(sql, Long.class).getSingleResult();
    }

    @Override
    public List<OrderJpaEntity> findByUserId(Long userId) {
        String sql = "SELECT o FROM OrderJpaEntity o WHERE o.user.id = :userId ORDER BY o.createdAt DESC";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setParameter("userId", userId);

        return query.getResultList();
    }

    /**
     * Busca una orden activa (PENDING) por el ID del usuario
     * @param userId ID del usuario
     * @return Optional con la orden activa o vacío si no existe
     */
    public Optional<OrderJpaEntity> findActiveOrderByUserId(Long userId) {
        String sql = "SELECT o FROM OrderJpaEntity o WHERE o.user.id = :userId AND o.orderStatus = :status";
        TypedQuery<OrderJpaEntity> query = entityManager
                .createQuery(sql, OrderJpaEntity.class)
                .setParameter("userId", userId)
                .setParameter("status", OrderStatus.PENDING);

        List<OrderJpaEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Obtiene el estado de una orden por su ID
     * @param orderId ID de la orden
     * @return Optional con el estado o vacío si no existe la orden
     */
    public Optional<OrderStatus> getOrderStatus(Long orderId) {
        String sql = "SELECT o.orderStatus FROM OrderJpaEntity o WHERE o.id = :orderId";
        TypedQuery<OrderStatus> query = entityManager
                .createQuery(sql, OrderStatus.class)
                .setParameter("orderId", orderId);

        List<OrderStatus> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}

