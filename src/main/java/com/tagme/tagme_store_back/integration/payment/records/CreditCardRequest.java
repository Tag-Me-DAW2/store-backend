package com.tagme.tagme_store_back.integration.payment.records;

public record CreditCardRequest(
        String number,
        String expirationDate,
        String cvv,
        String fullName
) {
}
