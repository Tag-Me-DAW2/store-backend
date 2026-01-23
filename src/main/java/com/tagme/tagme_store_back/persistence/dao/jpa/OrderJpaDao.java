package com.tagme.tagme_store_back.persistence.dao.jpa;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;

import java.util.List;

public interface OrderJpaDao extends GenericJpaDao<OrderJpaEntity> {
    List<OrderJpaEntity> findByUserId(Long userId);
}
