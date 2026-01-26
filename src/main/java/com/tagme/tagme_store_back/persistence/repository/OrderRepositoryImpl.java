package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.repository.OrderRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.OrderJpaDao;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaDao orderJpaDao;

    public OrderRepositoryImpl(OrderJpaDao orderJpaDao) {
        this.orderJpaDao = orderJpaDao;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {


//        if (orderDto.id() != null) {
//            return orderJpaDao.update(orderDto);
//        }
        return orderDto;
    }

    @Override
    public void delete(Long orderId) {

    }

    @Override
    public Optional<OrderStatus> getStatus(Long orderId) {
        return Optional.empty();
    }

    @Override
    public Optional<OrderDto> getActiveOrder(Long userId) {
        return Optional.empty();
    }
}
