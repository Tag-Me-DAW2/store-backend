package com.tagme.tagme_store_back.controller.webModel.request;

public record CreditCardRequest(
        String cardNumber,
        String cardHolderName,
        String cvv,
        String expirationDate
) {
}
