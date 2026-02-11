package com.tagme.tagme_store_back.persistence.dao.jpa;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.PaymentInfoJpaEntity;

import java.util.Optional;

public interface PaymentInfoJpaDao {
    PaymentInfoJpaEntity save(PaymentInfoJpaEntity entity);
    Optional<PaymentInfoJpaEntity> findByOrderId(Long orderId);
    void deleteByOrderId(Long orderId);
}
