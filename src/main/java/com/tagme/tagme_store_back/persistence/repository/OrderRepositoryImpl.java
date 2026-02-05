package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
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
        validateOrderDto(orderDto);

        OrderJpaEntity entity = OrderMapper.toJpaEntity(orderDto);

        if (orderDto.id() == null) {
            return OrderMapper.fromJpaEntity(orderJpaDao.insert(entity));
        } else {
            return OrderMapper.fromJpaEntity(orderJpaDao.update(entity));
        }
    }

    @Override
    public void delete(Long orderId) {
        validateOrderId(orderId);
        orderJpaDao.deleteById(orderId);
    }

    @Override
    public Optional<OrderStatus> getStatus(Long orderId) {
        validateOrderId(orderId);
        return orderJpaDao.getOrderStatus(orderId);
    }

    @Override
    public Optional<OrderDto> getActiveOrder(Long userId) {
        validateUserId(userId);
        return orderJpaDao.findActiveOrderByUserId(userId)
                .map(OrderMapper::fromJpaEntity);
    }

    // ==================== Métodos de validación ====================

    private void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new ValidationException("Order ID cannot be null");
        }
        if (orderId <= 0) {
            throw new ValidationException("Order ID must be a positive number");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new ValidationException("User ID cannot be null");
        }
        if (userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }
    }

    private void validateOrderDto(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("Order DTO cannot be null");
        }
        if (orderDto.user() == null) {
            throw new ValidationException("User cannot be null in order");
        }
        if (orderDto.orderStatus() == null) {
            throw new ValidationException("Order status cannot be null");
        }
    }
}
