package com.tagme.tagme_store_back.domain.repository;

import java.util.UUID;

public interface AuthRepository {
    UUID createSession(Long userId);
    void logout(String sessionId);
}
