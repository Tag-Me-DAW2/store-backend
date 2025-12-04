package com.tagme.tagme_store_back.persistence.dao.jpa;

import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;

import java.util.Optional;

public interface UserJpaDao extends GenericDao<UserJpaEntity>{
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByToken(String token);
}
