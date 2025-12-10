package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.mapper.UserMapper;
import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.LoginDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.service.AuthService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class LoginTests {
        @DisplayName("Given valid login credentials, when login is called, then a token is returned")
        @Test
        void loginWithValidCredentials() throws Exception {
            LoginRequest loginRequest = Instancio.of(LoginRequest.class).create();
            UUID uuid = UUID.randomUUID();

            when(authService.login(any(LoginDto.class))).thenReturn(uuid.toString());
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(loginRequest);

            mockMvc.perform(post("/auth/login")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        String responseBody = result.getResponse().getContentAsString();
                        assert responseBody.equals(uuid.toString());
                    });
        }

        @DisplayName("Given invalid login credentials, when login is called, then an unauthorized status is returned")
        @Test
        void loginWithInvalidCredentials() throws Exception {
            LoginRequest loginRequest = Instancio.of(LoginRequest.class).create();
            when(authService.login(any(LoginDto.class))).thenThrow(new InvalidCredentialsException("Invalid credentials"));

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(loginRequest);

            mockMvc.perform(post("/auth/login")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class LogoutTests {
        @DisplayName("Given a valid token, when logout is called, then no content status is returned")
        @Test
        void logoutWithValidToken() throws Exception {
            String token = "valid-token";
            UserDto UserDto = Instancio.of(UserDto.class).create();

            doNothing().when(authService).logout(anyString());
            when(authService.getByToken(anyString())).thenReturn(UserDto); // Sin esto el filtro Auth te dice que hasta la próxima

            mockMvc.perform(post("/auth/logout")
                            .header("Authorization", "Bearer " + token))
                            .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @DisplayName("Given an invalid token, when logout is called, then an unauthorized status is returned")
        @Test
        void logoutWithInvalidToken() throws Exception {
            String token = "invalid-token";
            doThrow(new InvalidCredentialsException("Invalid token")).when(authService).logout(token);

            mockMvc.perform(post("/auth/logout")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isUnauthorized());
        }
    }
}
