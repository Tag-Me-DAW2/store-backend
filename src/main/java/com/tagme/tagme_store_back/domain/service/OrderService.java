package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(Long userId);
}
