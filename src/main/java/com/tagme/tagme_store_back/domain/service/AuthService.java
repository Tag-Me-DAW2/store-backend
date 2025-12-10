package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
    void logout(String token);
}
