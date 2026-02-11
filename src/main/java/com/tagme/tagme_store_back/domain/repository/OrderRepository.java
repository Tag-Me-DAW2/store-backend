package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    OrderDto save(OrderDto orderDto);
    void delete(Long orderId);
    Optional<OrderStatus> getStatus(Long orderId);
    Optional<OrderDto> getActiveOrder(Long userId);
    List<OrderDto> getOrdersByUserId(Long userId);
    Page<OrderDto> getAllOrders(int page, int size, OrderStatus status, Long userId);
    Optional<OrderDto> getById(Long orderId);
    Long countAll();
}
