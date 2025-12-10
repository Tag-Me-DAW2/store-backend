package com.tagme.tagme_store_back.domain.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;

import java.util.Optional;

public interface UserRepository {
    Optional<UserDto> findById(Long id);
    UserDto save(UserDto userDto);
    void deleteById(Long id);
    Optional<UserDto> findByEmail(String email);
}
