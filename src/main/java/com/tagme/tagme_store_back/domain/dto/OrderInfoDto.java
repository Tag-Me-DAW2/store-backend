package com.tagme.tagme_store_back.domain.dto;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record OrderInfoDto(
        @Min(value = 0, message = "Shipping cost must be non-negative")
        BigDecimal shippingCost,
        @Min(value = 0, message = "Subtotal must be non-negative")
        BigDecimal subtotal,
        @Min(value = 0, message = "Total must be non-negative")
        BigDecimal total
) {
}
