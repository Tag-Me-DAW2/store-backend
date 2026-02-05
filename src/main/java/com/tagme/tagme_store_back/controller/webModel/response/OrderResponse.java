package com.tagme.tagme_store_back.controller.webModel.response;

import com.tagme.tagme_store_back.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        OrderStatus orderStatus,
        List<OrderItemResponse> orderItems,
        BigDecimal totalPrice,
        LocalDateTime paidDate
) {
}
