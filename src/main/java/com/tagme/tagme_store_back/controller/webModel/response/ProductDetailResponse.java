package com.tagme.tagme_store_back.controller.webModel.response;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Blob;

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
