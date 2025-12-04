package com.tagme.tagme_store_back.persistence.dao.jpa;

import java.util.UUID;

public interface AuthJpaDao {
    UUID createSession(Long userId);
    void logout(String token);
}
