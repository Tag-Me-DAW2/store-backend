package com.tagme.tagme_store_back.domain.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.sql.Blob;

public record ProductDto(
        Long id,
        @NotBlank(message = "Name cannot be empty or null")
        String name,
        @NotBlank(message = "Description cannot be empty or null")
        String description,
        @NotNull(message = "Base price cannot be null")
        @DecimalMin(value = "0.0", message = "Base price must be non-negative")
        BigDecimal basePrice,
        @NotNull(message = "Discount percentage cannot be null")
        @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
        @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
        BigDecimal discountPercentage,
        BigDecimal price,
        @NotNull(message = "Image cannot be null")
        Blob image,
        @NotNull(message = "Image name cannot be null")
        String imageName,
        @NotNull
        CategoryDto category
) {
}
