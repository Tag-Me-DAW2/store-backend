package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.persistence.dao.jpa.PaymentInfoJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.PaymentInfoJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class PaymentInfoJpaDaoImpl implements PaymentInfoJpaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PaymentInfoJpaEntity save(PaymentInfoJpaEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad PaymentInfo no puede ser nula");
        }

        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    @Override
    public Optional<PaymentInfoJpaEntity> findByOrderId(Long orderId) {
        if (orderId == null) {
            return Optional.empty();
        }

        String sql = "SELECT p FROM PaymentInfoJpaEntity p WHERE p.order.id = :orderId";
        TypedQuery<PaymentInfoJpaEntity> query = entityManager
                .createQuery(sql, PaymentInfoJpaEntity.class)
                .setParameter("orderId", orderId);

        List<PaymentInfoJpaEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        if (orderId == null) {
            return;
        }

        String sql = "DELETE FROM PaymentInfoJpaEntity p WHERE p.order.id = :orderId";
        entityManager.createQuery(sql)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }
}
