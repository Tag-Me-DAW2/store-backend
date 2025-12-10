package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.controller.webModel.request.LoginRequest;
import com.tagme.tagme_store_back.domain.dto.LoginDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.utils.PasswordUtils;
import jakarta.transaction.Transactional;

public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthRepository authRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @Override
    @Transactional
    public String login(LoginDto loginDto) {
        UserDto userDto = userRepository.findByEmail(loginDto.email()).orElseThrow(() ->
                new InvalidCredentialsException("Email o contraseña incorrectos")
        );

        boolean validPassword = PasswordUtils.verifyPassword(
                loginDto.password(),
                userDto.password()
        );

        if (!validPassword) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        return authRepository.createSession(userDto.id()).toString();
    }

    @Override
    @Transactional
    public void logout(String token) {
        if (token == null ||  token.isBlank()) {
            throw new InvalidCredentialsException("Token inválido");
        }

        authRepository.logout(token);
    }
}
