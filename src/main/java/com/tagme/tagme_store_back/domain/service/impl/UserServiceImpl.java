package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.UserService;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getById(Long id) {
        if (id == null) {
            throw new BusinessException("User id cannot be null");
        }

        return userRepository.findById(id).orElseThrow(
                () -> new BusinessException("User with id " + id + " not found")
        );
    }

    @Override
    public UserDto create(UserDto userDto) {
        if(userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new BusinessException("Email " + userDto.email() + " is already in use");
        }

        return userRepository.save(userDto);
    }

    @Override
    public UserDto update(UserDto userDto) {
        if(userRepository.findById(userDto.id()).isEmpty()) {
            throw new BusinessException("Email " + userDto.email() + " not found");
        }

        userRepository.findByEmail(userDto.email())
                .filter(u -> !u.id().equals(userDto.id()))
                .ifPresent(u -> {
                    throw new BusinessException("Email " + userDto.email() + " is already in use");
                });

        return userRepository.save(userDto);
    }

    @Override
    public UserDto getByToken(String token) {
        return userRepository.findByToken(token).orElse(null);
    }

    @Override
    public void delete(Long id) {
        UserDto userDto = getById(id);

        if(userDto == null) throw new BusinessException("User with id " + id + " not found");

        userRepository.deleteById(id);
    }
}
