package com.tagme.tagme_store_back.controller.webModel.response;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        ProductSummaryResponse product,
        Long quantity,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        BigDecimal total
) {
}

