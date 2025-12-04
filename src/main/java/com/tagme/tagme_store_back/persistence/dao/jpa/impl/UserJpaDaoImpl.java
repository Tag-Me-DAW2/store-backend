package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;

import java.util.Optional;

public class UserJpaDaoImpl implements UserJpaDao {
    @Override
    public Optional<UserJpaEntity> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserJpaEntity> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<UserJpaEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public UserJpaEntity save(UserJpaEntity entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
