package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.UserMapper;
import com.tagme.tagme_store_back.domain.model.User;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.utils.PasswordUtils;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getById(Long id) {
        if (id == null) {
            throw new RuntimeException("User id cannot be null");
        }

        return userRepository.findById(id)
                .map(UserMapper::fromUserDtoToUser)
                .map(UserMapper::fromUserToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDto create(UserDto userDto) {
        if(userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new BusinessException("Email " + userDto.email() + " is already in use");
        }

        User userModel = UserMapper.fromUserDtoToUser(userDto);
        userModel.setPassword(PasswordUtils.hashPassword(userModel.getPassword()));
        UserDto userHashed = UserMapper.fromUserToUserDto(userModel);

        return userRepository.save(userHashed);
    }

    @Override
    public UserDto update(UserDto userDto) {
        if(userRepository.findById(userDto.id()).isEmpty()) {
            throw new ResourceNotFoundException("Email " + userDto.email() + " not found");
        }

        userRepository.findByEmail(userDto.email())
                .filter(u -> !u.id().equals(userDto.id()))
                .ifPresent(u -> {
                    throw new BusinessException("Email " + userDto.email() + " is already in use");
                });

        User userModel = UserMapper.fromUserDtoToUser(userDto);
        UserDto userToUpdate = UserMapper.fromUserToUserDto(userModel);

        return userRepository.save(userToUpdate);
    }

    @Override
    public UserDto getByToken(String token) {
        return userRepository.findByToken(token).orElse(null);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new RuntimeException("User id cannot be null");
        }

        UserDto userDto = getById(id);
        if(userDto == null) throw new ResourceNotFoundException("User with id " + id + " not found");

        userRepository.deleteById(id);
    }
}
