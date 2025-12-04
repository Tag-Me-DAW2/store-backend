package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.SessionJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuthJpaDaoImpl implements AuthJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UUID createSession(Long userId) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        SessionJpaEntity sessionJpaEntity = new SessionJpaEntity(userId, uuid.toString(), createdAt);
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
}
