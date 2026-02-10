package com.tagme.tagme_store_back.persistence.dao.jpa;

import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;

import java.util.List;
import java.util.Optional;

public interface OrderJpaDao extends GenericJpaDao<OrderJpaEntity> {
    List<OrderJpaEntity> findByUserId(Long userId);
    Optional<OrderJpaEntity> findActiveOrderByUserId(Long userId);
    Optional<OrderStatus> getOrderStatus(Long orderId);
    List<OrderJpaEntity> findNonActiveOrdersByUserId(Long userId);
}
