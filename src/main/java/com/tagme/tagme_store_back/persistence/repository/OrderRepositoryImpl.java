package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.OrderJpaEntity;
import com.tagme.tagme_store_back.persistence.mapper.OrderMapper;

import java.util.List;
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

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        validateUserId(userId);
        return orderJpaDao.findNonActiveOrdersByUserId(userId)
                .stream()
                .map(OrderMapper::fromJpaEntity)
                .toList();
    }

    @Override
    public Page<OrderDto> getAllOrders(int page, int size, OrderStatus status, Long userId) {
        List<OrderDto> orders = orderJpaDao.findAllWithFilters(page, size, status, userId)
                .stream()
                .map(OrderMapper::fromJpaEntity)
                .toList();
        
        Long totalElements = orderJpaDao.countWithFilters(status, userId);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        return new Page<>(orders, page, size, totalElements, totalPages);
    }

    @Override
    public Optional<OrderDto> getById(Long orderId) {
        validateOrderId(orderId);
        return orderJpaDao.findById(orderId)
                .map(OrderMapper::fromJpaEntity);
    }

    @Override
    public Long countAll() {
        return orderJpaDao.countWithFilters(null, null);
    }

    // ==================== Métodos de validación ====================

    private void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new ValidationException("El ID del pedido no puede ser nulo");
        }
        if (orderId <= 0) {
            throw new ValidationException("El ID del pedido debe ser un número positivo");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new ValidationException("El ID de usuario no puede ser nulo");
        }
        if (userId <= 0) {
            throw new ValidationException("El ID de usuario debe ser un número positivo");
        }
    }

    private void validateOrderDto(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("El pedido no puede ser nulo");
        }
        if (orderDto.user() == null) {
            throw new ValidationException("El usuario no puede ser nulo en el pedido");
        }
        if (orderDto.orderStatus() == null) {
            throw new ValidationException("El estado del pedido no puede ser nulo");
        }
    }
}
