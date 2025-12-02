package com.tagme.tagme_store_back.domain.service;

public interface User {
    UserDto getById(Long id);
    UserDto create(UserDto userDto);
    UserDto update(UserDto userDto);
    void delete(Long id);
}
