package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;

import java.util.Optional;

public interface OrderRepository {
    OrderDto save(OrderDto orderDto);
    void delete(Long orderId);
    Optional<OrderStatus> getStatus(Long orderId);
    Optional<OrderDto> getActiveOrder(Long userId);
}
