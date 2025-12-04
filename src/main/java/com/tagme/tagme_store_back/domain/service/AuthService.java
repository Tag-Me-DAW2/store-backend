package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.controller.webModel.LoginRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
    void logout(String token);
}
