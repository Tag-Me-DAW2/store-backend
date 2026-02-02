package com.tagme.tagme_store_back.domain.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.sql.Blob;

public record OrderItemDto(
        Long id,
        ProductDto productDto,
        String productName,
        Blob productImage,
        String productImageName,
        @Min(value = 1, message = "Quantity must be at least 1")
        Long quantity,
        @Min(value = 0, message = "Base price must be non-negative")
        BigDecimal basePrice,
        @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
        @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
        BigDecimal discountPercentage,
        BigDecimal total
) {
}
