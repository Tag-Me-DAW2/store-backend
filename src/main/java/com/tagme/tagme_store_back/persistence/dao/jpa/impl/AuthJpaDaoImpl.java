package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.SessionJpaEntity;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthJpaDaoImpl implements AuthJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UUID createSession(Long userId) {
        UserJpaEntity user = entityManager.find(UserJpaEntity.class, userId);
        if (user == null) {
            throw new ResourceNotFoundException("User with ID " + userId + " does not exist.");
        }

        UUID uuid = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        SessionJpaEntity sessionJpaEntity = new SessionJpaEntity(user, uuid.toString(), createdAt);
        entityManager.persist(sessionJpaEntity);

        return uuid;
    }

    @Override
    public void logout(String token) {
        SessionJpaEntity session = entityManager.createQuery(
                "SELECT s FROM SessionJpaEntity s WHERE s.token = :token", SessionJpaEntity.class)
                .setParameter("token", token)
                .getSingleResult();

        if (session != null) {
            entityManager.remove(session);
        }
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(s) FROM SessionJpaEntity s", Long.class).getSingleResult();
    }

    @Override
    public Optional<UserJpaEntity> findByToken(String token) {
        String sql = "SELECT s.user FROM SessionJpaEntity s WHERE s.token = :token";

        try {
            UserJpaEntity user = entityManager.createQuery(sql, UserJpaEntity.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
