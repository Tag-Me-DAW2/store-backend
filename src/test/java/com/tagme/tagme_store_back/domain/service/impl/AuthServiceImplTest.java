package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.utils.PasswordUtils;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private UserDto userDto;

    @Nested
    class LoginTests {
        @DisplayName("Test login method should work normally")
        @Test
        void testLoginSuccessful() {
            loginRequest = Instancio.of(LoginRequest.class).withSeed(10).set(Select.field("password"),"password123").create();
            userDto = Instancio.of(UserDto.class).withSeed(10).set(Select.field("password"), PasswordUtils.hashPassword("password123")).create();
            UUID uuid = UUID.randomUUID();

            when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userDto));
            when(authRepository.createSession(any())).thenReturn(uuid);

            assertEquals(uuid.toString(), authService.login(loginRequest).toString());
        }

        @DisplayName("Test login method should throw InvalidCredentialsException when email is incorrect or not present")
        @Test
        void testLoginInvalidEmail() {
            loginRequest = Instancio.of(LoginRequest.class).withSeed(20).set(field("password"), "password123").create();
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
        }

        @DisplayName("Test login method should throw InvalidCredentialsException when password is incorrect")
        @Test
        void testLoginInvalidPassword() {
            loginRequest = Instancio.of(LoginRequest.class).withSeed(30).set(field("password"), "wrongpassword").create();
            userDto = Instancio.of(UserDto.class).withSeed(30).set(field("password"), PasswordUtils.hashPassword("correctpassword")).create();
            when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userDto));
            assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
        }
    }

    @Nested
    class LogoutTests {
        @DisplayName("Test logout method should work normally")
        @Test
        void testLogoutSuccessful() {
            String token = UUID.randomUUID().toString();
            assertDoesNotThrow(() -> authService.logout(token));
        }

        @DisplayName("Test logout method should throw InvalidCredentialsException when token is null")
        @Test
        void testLogoutNullToken() {
            String token = null;
            assertThrows(InvalidCredentialsException.class, () -> authService.logout(token));
        }

        @DisplayName("Test logout method should throw InvalidCredentialsException when token is blank")
        @Test
        void testLogoutBlankToken() {
            String token = "   ";
            assertThrows(InvalidCredentialsException.class, () -> authService.logout(token));
        }
    }
}