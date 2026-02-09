package com.tagme.tagme_store_back.integration.payment.records;

public record CreditCardPaymentRequest(
    AuthRequest authRequest,
    CreditCardRequest creditCardRequest,
    TransferRequest transferRequest,
    PayRequest payRequest
) {
}
