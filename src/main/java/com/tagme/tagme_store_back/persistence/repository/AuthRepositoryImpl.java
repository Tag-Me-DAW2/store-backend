package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

public class AuthRepositoryImpl implements AuthRepository {
    private AuthJpaDao authJpaDao;

    public AuthRepositoryImpl(AuthJpaDao authDao) {
        this.authJpaDao = authDao;
    }

    @Override
    public UUID createSession(Long userId) {
        return authJpaDao.createSession(userId);
    }

    @Override
    public void logout(String token) {
        authJpaDao.logout(token);
    }

    @Override
    public Optional<UserDto> findByToken(String token) {
        return authJpaDao.findByToken(token).map(UserMapper::fromUserJpaEntityToUserDto);
    }
}
