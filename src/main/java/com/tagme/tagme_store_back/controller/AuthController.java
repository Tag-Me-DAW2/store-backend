package com.tagme.tagme_store_back.controller;

import com.tagme.tagme_store_back.controller.mapper.LoginMapper;
import com.tagme.tagme_store_back.controller.mapper.UserMapper;
import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.controller.webModel.request.VerifyPasswordRequest;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.LoginDto;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import com.tagme.tagme_store_back.web.context.AuthContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        DtoValidator.validate(loginRequest);

        LoginDto loginDto = LoginMapper.fromLoginRequestToLoginDto(loginRequest);
        String token = authService.login(loginDto);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.substring(7));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserByToken(@RequestHeader("Authorization") String token) throws IOException {
        UserResponse userResponse = UserMapper.fromUserDtoToUserResponse(authService.getByToken(token.substring(7)));
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestHeader ("Authorization") String token, @RequestBody VerifyPasswordRequest request) {
        DtoValidator.validate(request);

        Long userId = authService.getByToken(token.substring(7)).id();

        Boolean isCurrent = authService.isCurrentPassword(userId, request.password());
        return new ResponseEntity<>(isCurrent, HttpStatus.OK);
    }
}
