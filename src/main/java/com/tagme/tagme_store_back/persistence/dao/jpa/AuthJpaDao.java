package com.tagme.tagme_store_back.persistence.dao.jpa;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthJpaDao {
    UUID createSession(Long userId);
    void logout(String token);
    Optional<UserJpaEntity> findByToken(String token);
    Long count();
}
