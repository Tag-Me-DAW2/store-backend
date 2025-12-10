package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.LoginMapper;
import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.dto.LoginDto;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        DtoValidator.validate(loginRequest);

        LoginDto loginDto = LoginMapper.fromLoginRequestToLoginDto(loginRequest);
        String token = authService.login(loginDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.substring(7));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
