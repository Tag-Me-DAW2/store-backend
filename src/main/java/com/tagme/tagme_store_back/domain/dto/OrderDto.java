package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.model.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        @NotNull(message = "User cannot be null")
        UserDto user,
        @NotNull(message = "Order status cannot be null")
        OrderStatus orderStatus,
        List<OrderItemDto>orderItems,
        @Min(value = 0, message = "Total price must be non-negative")
        BigDecimal totalPrice,
        @Min(value = 0, message = "Shipping cost must be non-negative")
        BigDecimal shippingCost,
        ShippingInfoDto shippingInfo,
        LocalDateTime paidDate,
        LocalDateTime createdAt
) {
}
