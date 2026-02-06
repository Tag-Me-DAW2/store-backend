package com.tagme.tagme_store_back.EpsteinFiles.payment.records;

public record CreditCardRequest(
        String number,
        String expirationDate,
        String cvv,
        String fullName
) {
}
