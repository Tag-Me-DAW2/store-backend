package com.tagme.tagme_store_back.EpsteinFiles.payment;

import com.tagme.tagme_store_back.EpsteinFiles.payment.records.CreditCardRequest;

import java.math.BigDecimal;

public interface CreditCardPaymentService {
    void execute(CreditCardRequest creditCard, BigDecimal amount);
}
