package com.tagme.tagme_store_back.controller.webModel.request;

public record LoginRequest(
    String email,
    String password
) {
}
