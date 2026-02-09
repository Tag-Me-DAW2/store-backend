package com.tagme.tagme_store_back.EpsteinFiles.payment.impl;

import com.tagme.tagme_store_back.EpsteinFiles.http.HttpClientService;
import com.tagme.tagme_store_back.EpsteinFiles.http.response.ApiResponse;
import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.EpsteinFiles.payment.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreditCardPaymentServiceImpl implements CreditCardPaymentService {

    @Autowired
    private HttpClientService httpClient;

    @Value("${tagme.bank.api.host}")
    private String bankApiHost;

    @Value("${tagme.bank.api.key}")
    private String bankApiKey;

    @Value("${tagme.bank.api.username}")
    private String bankApiUsername;

    @Value("${tagme.bank.api.iban}")
    private String bankApiIban;

    @Override
    public void execute(CreditCardRequest creditCard, BigDecimal amount) {
        AuthRequest authRequest = new AuthRequest(bankApiKey, bankApiUsername);
        TransferRequest transferRequest = new TransferRequest(bankApiIban);
        String concept = "Compra en Tagme Store";
        PayRequest payRequest = new PayRequest(amount, concept);

        CreditCardPaymentRequest paymentRequest =
                new CreditCardPaymentRequest(authRequest, creditCard, transferRequest, payRequest);

        ApiResponse response = httpClient.post(
                bankApiHost + "/payments/credit-card",
                paymentRequest
        );

        if (!response.isOk()) {
            throw new RuntimeException(
                    response.getBody()
            );
        }
    }
}
