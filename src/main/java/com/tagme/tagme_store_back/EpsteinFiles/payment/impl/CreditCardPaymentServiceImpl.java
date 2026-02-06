package com.tagme.tagme_store_back.EpsteinFiles.payment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.EpsteinFiles.payment.CreditCardPaymentService;
import com.tagme.tagme_store_back.EpsteinFiles.payment.records.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.math.BigDecimal;

public class CreditCardPaymentServiceImpl implements CreditCardPaymentService {

    @Override
    public void execute(CreditCardRequest creditCard, BigDecimal amount) {
        // Crear datos necesarios para la llamada a la API de pago con tarjeta de crédito
        String apiKey = "gurt_el_cartomago";
        String username = "TagMe";
        AuthRequest authRequest = new AuthRequest(apiKey, username);

        String iban = "ES9121000418450200051332";
        TransferRequest transferRequest = new TransferRequest(iban);

        String concept = "Compra en Tagme Store";
        PayRequest payRequest = new PayRequest(amount, concept);

        // Realizar la llamada a la API de pago con tarjeta de crédito utilizando los datos creados
        CreditCardPaymentRequest paymentRequest = new CreditCardPaymentRequest(authRequest, creditCard, transferRequest, payRequest);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8081/payments/credit-card");

        System.out.println(paymentRequest);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(paymentRequest);

            HttpEntity entity =
                    new StringEntity(json, ContentType.APPLICATION_JSON);

            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
                if (response.getCode() == 200) {
                    System.out.println("Pago con tarjeta de crédito realizado con éxito");
                } else {
                    System.out.println("Error al realizar el pago con tarjeta de crédito: " + response.getCode());
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
