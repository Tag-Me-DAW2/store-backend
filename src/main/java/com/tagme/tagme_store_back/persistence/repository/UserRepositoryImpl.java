package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private UserJpaDao userJpaDao;

    public UserRepositoryImpl(UserJpaDao userJpaDao) {
        this.userJpaDao = userJpaDao;
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public UserDto save(UserDto userDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<UserDto> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return Optional.empty();
    }
}
