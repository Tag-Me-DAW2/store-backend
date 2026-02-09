package com.tagme.tagme_store_back.integration.payment;

import com.tagme.tagme_store_back.integration.payment.records.CreditCardRequest;

import java.math.BigDecimal;

public interface CreditCardPaymentService {
    void execute(CreditCardRequest creditCard, BigDecimal amount);
}
