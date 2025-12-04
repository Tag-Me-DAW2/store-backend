package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.UserDto;

public interface UserService {
    UserDto getById(Long id);
    UserDto create(UserDto userDto);
    UserDto update(UserDto userDto);
    UserDto getByToken(String token);
    void deleteById(Long id);
}
