package com.tagme.tagme_store_back.integration.http;

import com.tagme.tagme_store_back.integration.http.response.ApiResponse;

public interface HttpClientService {
    ApiResponse post(String url, Object requestBody);
}
