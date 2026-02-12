package com.tagme.tagme_store_back.EpsteinFiles.http.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.EpsteinFiles.http.HttpClientService;
import com.tagme.tagme_store_back.EpsteinFiles.http.exception.ApiNotWorkingException;
import com.tagme.tagme_store_back.EpsteinFiles.http.response.ApiResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class HttpClientServiceImpl implements HttpClientService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final CloseableHttpClient httpClient;
    private final RequestConfig defaultRequestConfig;

    // timeouts en ms (configurables desde application.properties)
    public HttpClientServiceImpl(
            @Value("${http.client.connect-timeout-ms:60000}") long connectTimeoutMs,
            @Value("${http.client.response-timeout-ms:60000}") long responseTimeoutMs,
            @Value("${http.client.connection-request-timeout-ms:60000}") long connectionRequestTimeoutMs
    ) {
        this.defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectTimeoutMs))
                .setResponseTimeout(Timeout.ofMilliseconds(responseTimeoutMs))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeoutMs))
                .build();

        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }

    @Override
    public ApiResponse post(String url, Object requestBody) {
        HttpPost httpPost = new HttpPost(url);

        try {
            String json = mapper.writeValueAsString(requestBody);
            httpPost.setEntity(
                    new StringEntity(json, ContentType.APPLICATION_JSON)
            );

            // Si la URL contiene '?longTimeout=true' aplicamos un timeout de respuesta mucho mayor (10 min)
            if (url != null && url.contains("longTimeout=true")) {
                RequestConfig longReq = RequestConfig.copy(defaultRequestConfig)
                        .setResponseTimeout(Timeout.ofMinutes(10))
                        .build();
                httpPost.setConfig(longReq);
            }

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                int statusCode = response.getCode();

                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode >= 400 && body != null) {
                    JsonNode jsonNode = mapper.readTree(body);
                    body = jsonNode.get("message").asText();
                }

                return new ApiResponse(statusCode, body);
            }

        } catch (Exception e) {
            throw new ApiNotWorkingException(e.getMessage());
        }
    }
}
