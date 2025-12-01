package com.tagme.tagme_store_back.domain.service;

public interface User {
    UserDto getById(Long id);
    UserDto login(String email, String password);
    UserDto create(UserDto userDto);
    UserDto update(UserDto userDto);
    void delete(Long id);
}
