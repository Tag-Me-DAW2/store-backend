package com.tagme.tagme_store_back.controller.webModel.request;

import java.math.BigDecimal;

public record ProductInsertRequest(
        String name,
        String description,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        String image,
        String imageName,
        Long categoryId,
        String material
) {
}
