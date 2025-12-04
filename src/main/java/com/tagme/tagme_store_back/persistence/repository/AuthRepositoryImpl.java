package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.repository.AuthRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;

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
    public boolean isValidToken(String token) {
        return authJpaDao.isValidToken(token);
    }
}
