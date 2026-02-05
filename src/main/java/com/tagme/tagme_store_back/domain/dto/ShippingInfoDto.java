package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ShippingInfoDto(
        Long id,
        Long orderId,
        @NotBlank(message = "Name cannot be empty or null")
        String name,
        @NotBlank(message = "Surname cannot be empty or null")
        String surname,
        @ValidEmail
        String email,
        @NotBlank(message = "Address cannot be empty or null")
        String address,
        @NotBlank(message = "City cannot be empty or null")
        String city,
        @NotBlank(message = "Postal code cannot be empty or null")
        String postalCode,
        @NotBlank(message = "Country cannot be empty or null")
        String country,
        LocalDateTime createdAt
) {
}
