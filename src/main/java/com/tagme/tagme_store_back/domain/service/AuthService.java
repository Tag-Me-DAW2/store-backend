package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.dto.LoginDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;

public interface AuthService {
    String login(LoginDto loginDto);
    void logout(String token);
    UserDto getByToken(String token);
    Boolean isCurrentPassword(Long userId, String currentPassword);
}
