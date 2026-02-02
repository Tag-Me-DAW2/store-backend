package com.tagme.tagme_store_back.controller.webModel.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        ProductSummaryResponse product,
        String productName,
        String productImage,
        String productImageName,
        Long quantity,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        BigDecimal total) {
}
