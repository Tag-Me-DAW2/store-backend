package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.model.Page;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(Long userId);
    Page<OrderDto> getAllOrders(int page, int size, OrderStatus status, Long userId);
    void retryPayment(Long orderId);
    Long getTotalOrders();
}
