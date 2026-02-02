package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.mapper.OrderMapper;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaDao orderJpaDao;

    public OrderRepositoryImpl(OrderJpaDao orderJpaDao) {
        this.orderJpaDao = orderJpaDao;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        OrderJpaEntity entity = OrderMapper.toJpaEntity(orderDto);

        if (orderDto.id() == null) {
            return OrderMapper.fromJpaEntity(orderJpaDao.insert(entity));
        } else {
            return OrderMapper.fromJpaEntity(orderJpaDao.update(entity));
        }
    }

    @Override
    public void delete(Long orderId) {
        orderJpaDao.deleteById(orderId);
    }

    @Override
    public Optional<OrderStatus> getStatus(Long orderId) {
        return orderJpaDao.getOrderStatus(orderId);
    }

    @Override
    public Optional<OrderDto> getActiveOrder(Long userId) {
        return orderJpaDao.findActiveOrderByUserId(userId)
                .map(OrderMapper::fromJpaEntity);
    }
}
