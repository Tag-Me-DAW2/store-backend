package com.tagme.tagme_store_back.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CreditCardDto(
        @NotBlank(message = "Card number cannot be empty or null")
        String cardNumber,
        @NotBlank(message = "Card holder name cannot be empty or null")
        String cardHolderName,
        @NotBlank(message = "CVV cannot be empty or null")
        String cvv,
        @NotBlank(message = "Expiration date cannot be empty or null")
        String expirationDate
) {
}
