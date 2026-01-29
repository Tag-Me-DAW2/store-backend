package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.Page;

import java.util.Optional;

public interface UserRepository {
    Page<UserDto> findAll(int page, int size);
    Optional<UserDto> findById(Long id);
    UserDto save(UserDto userDto);
    void deleteById(Long id);
    Optional<UserDto> findByEmail(String email);
    void updatePassword(UserDto userDto);
}
