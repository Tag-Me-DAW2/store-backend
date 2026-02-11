package com.tagme.tagme_store_back.domain.dto;

public record PaymentInfoDto(
        Long id,
        Long orderId,
        String cardNumber,
        String cardHolderName,
        String cvv,
        String expirationDate
) {
}
