package com.tagme.tagme_store_back.EpsteinFiles.payment.records;

public record CreditCardPaymentRequest(
    AuthRequest authRequest,
    CreditCardRequest creditCardRequest,
    TransferRequest transferRequest,
    PayRequest payRequest
) {
}
