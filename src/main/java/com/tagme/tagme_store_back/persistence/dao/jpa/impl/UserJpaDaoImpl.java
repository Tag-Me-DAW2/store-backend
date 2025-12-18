package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UserJpaDaoImpl implements UserJpaDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserJpaEntity> findAll(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);

        String sql = "SELECT u FROM UserJpaEntity u ORDER BY u.id";
        TypedQuery<UserJpaEntity> query = entityManager
                .createQuery(sql, UserJpaEntity.class)
                .setFirstResult(pageIndex * size)
                .setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Optional<UserJpaEntity> findByEmail(String email) {
        String sql = "SELECT u FROM UserJpaEntity u WHERE u.email = :email";

        try {
            UserJpaEntity user = entityManager.createQuery(sql, UserJpaEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public UserJpaEntity insert(UserJpaEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public UserJpaEntity update(UserJpaEntity entity) {

        UserJpaEntity managed = entityManager.find(UserJpaEntity.class, entity.getId());

        if (managed == null) {
            throw new ResourceNotFoundException("User not found");
        }

        managed.setUsername(entity.getUsername());
        managed.setEmail(entity.getEmail());
        managed.setFirstName(entity.getFirstName());
        managed.setLastName(entity.getLastName());
        managed.setPhone(entity.getPhone());
        managed.setRole(entity.getRole());
        managed.setProfilePicture(entity.getProfilePicture());

        return managed;
    }

    @Override
    public void deleteById(Long id) {
        UserJpaEntity managed = entityManager.find(UserJpaEntity.class, id);
        if (managed == null) {
            throw new ResourceNotFoundException("User not found");
        }
        entityManager.remove(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM UserJpaEntity u", Long.class).getSingleResult();
    }
}
