package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.service.AuthService;
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
        String token = authService.login(loginRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.replace("Bearer ", ""));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
