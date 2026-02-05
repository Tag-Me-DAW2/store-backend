package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.mapper.UserMapper;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.User;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.utils.PasswordUtils;
import jakarta.transaction.Transactional;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDto> getAll(int page, int size) {
        if(page < 1 || size < 1) {
            throw new BusinessException("Page and size must be greater than 0");
        }

        Page<UserDto> userDtoPage = userRepository.findAll(page, size);

        List<UserDto> itemsDto = userDtoPage.data()
                .stream()
                .map(UserMapper::fromUserDtoToUser)
                .map(UserMapper::fromUserToUserDto)
                .toList();

        return new Page<>(
                itemsDto,
                userDtoPage.pageNumber(),
                userDtoPage.pageSize(),
                userDtoPage.totalElements()
        );
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
    @Transactional
    public UserDto create(UserDto userDto) {
        if(userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new BusinessException("Email " + userDto.email() + " is already in use");
        }

        if (userDto.password().isEmpty()) {
            throw new BusinessException("Password cannot be empty");
        }

        User userModel = UserMapper.fromUserDtoToUser(userDto);
        userModel.setPassword(PasswordUtils.hashPassword(userModel.getPassword()));
        UserDto userHashed = UserMapper.fromUserToUserDto(userModel);

        return userRepository.save(userHashed);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new RuntimeException("User id cannot be null");
        }

        getById(id); // Verify user exists

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        UserDto userDto = getById(userId);
        User userModel = UserMapper.fromUserDtoToUser(userDto);
        userModel.setPassword(PasswordUtils.hashPassword(newPassword));
        UserDto userToUpdate = UserMapper.fromUserToUserDto(userModel);

        userRepository.updatePassword(userToUpdate);
    }
}
