package com.tagme.tagme_store_back.controller.webModel.response;

import java.math.BigDecimal;

public record ProductSummaryResponse(
        Long id,
        String name,
        BigDecimal discountPercentage,
        BigDecimal price,
        String image,
        CategoryResponse category,
        String material
) {
}
