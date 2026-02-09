package com.tagme.tagme_store_back.EpsteinFiles.http;

import com.tagme.tagme_store_back.EpsteinFiles.http.response.ApiResponse;

public interface HttpClientService {
    ApiResponse post(String url, Object requestBody);
}
