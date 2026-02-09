package com.tagme.tagme_store_back.domain.dto;

import jakarta.validation.Valid;

public record PayCartDto(
        @Valid
        OrderInfoDto orderInfo,
        @Valid
        CreditCardDto paymentInfo,
        @Valid
        ShippingInfoDto shippingInfo
) {
}
