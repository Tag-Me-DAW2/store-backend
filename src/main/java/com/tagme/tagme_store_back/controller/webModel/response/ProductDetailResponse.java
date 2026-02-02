package com.tagme.tagme_store_back.controller.webModel.response;

import java.math.BigDecimal;

public record ProductDetailResponse(
        Long id,
        String name,
        String description,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        BigDecimal price,
        String image,
        String imageName,
        CategoryResponse category,
        String material
) {
}
