package com.tagme.tagme_store_back.controller.webModel;

public record LoginRequest(
    String email,
    String password
) {
}
