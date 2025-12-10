package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.dto.LoginDto;

public class LoginMapper {
    public static LoginDto fromLoginRequestToLoginDto (LoginRequest loginRequest) {
        if (loginRequest == null) {
            return null;
        }
        return new LoginDto(
                loginRequest.email(),
                loginRequest.password()
        );
    }
}
