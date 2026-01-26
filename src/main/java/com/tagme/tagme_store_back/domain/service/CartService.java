package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.OrderDto;
import com.tagme.tagme_store_back.domain.model.OrderStatus;

public interface CartService {
    void clearCart(Long userId);
    void createCart(Long userId);
    OrderDto getActiveCart(Long userId);
    OrderStatus getCartStatus(Long userId);
    void updatePendingCart(OrderDto orderDto);
    void updateCart(OrderDto orderDto);
}
