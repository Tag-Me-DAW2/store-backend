package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.model.Category;

import java.math.BigDecimal;
import java.sql.Blob;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal basePrice,
        BigDecimal discountPercentage,
        Blob image,
        CategoryDto category
) {
}
