package com.tagme.tagme_store_back.domain.service;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.Page;

public interface UserService {
    Page<UserDto> getAll(int page, int size);
    UserDto getById(Long id);
    UserDto create(UserDto userDto);
    UserDto update(UserDto userDto);
    void deleteById(Long id);
}
