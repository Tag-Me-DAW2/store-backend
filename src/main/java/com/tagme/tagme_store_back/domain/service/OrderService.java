package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;

public interface OrderService {
    OrderDto create(OrderDto orderDto);
    OrderDto update(OrderDto orderDto);
    void deleteById(Long orderId);
    OrderStatus getStatus(OrderDto orderDto);
    OrderDto getActiveOrder(UserDto userDto);
}
