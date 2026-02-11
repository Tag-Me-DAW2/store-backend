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
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class HttpClientServiceImpl implements HttpClientService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final CloseableHttpClient httpClient;

    public HttpClientServiceImpl() {
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public ApiResponse post(String url, Object requestBody) {
        HttpPost httpPost = new HttpPost(url);

        try {
            String json = mapper.writeValueAsString(requestBody);
            httpPost.setEntity(
                    new StringEntity(json, ContentType.APPLICATION_JSON)
            );

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
