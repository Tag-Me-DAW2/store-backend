package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.annotation.DaoTest;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DaoTest
class AuthJpaDaoImplTest extends BaseJpaDaoTest<AuthJpaDao> {

    @Nested
    class CreateSessionTests {
        @DisplayName("Create Session with Valid User ID should return UUID")
        @Test
        void createSession_ValidUserId_ShouldReturnUUID() {
            Long userId = 1L;
            UUID result = dao.createSession(userId);
            assertNotNull(result);
        }

        @DisplayName("If user does not exist, should throw exception")
        @Test
        void createSession_NonExistentUserId_ShouldThrowException() {
            Long nonExistentUserId = 999L;
            assertThrows(ResourceNotFoundException.class, () -> dao.createSession(nonExistentUserId));
        }
    }

    @Nested
    class DeleteSessionTests {
        @DisplayName("Logout with Valid Token should remove session")
        @Test
        void logout_ValidToken_ShouldNotThrowException() {
            String validToken = "example-token-1234567890";
            Long totalSessionsBefore = dao.count();
            dao.logout(validToken);
            Long totalSessionsAfter = dao.count();

            assertEquals(totalSessionsBefore - 1, totalSessionsAfter);
        }
    }

    @Nested
    class FindByTokenTests {
        @DisplayName("Given an existing token, when findByToken is called, then the corresponding user is returned")
        @Test
        void findByExistingToken() {
            String token = "example-token-1234567890";
            UserJpaEntity expectedUser = dao.findByToken(token).get();

            assertNotNull(expectedUser);
        }

        @DisplayName("Given a non-existing token, when findByToken is called, then an empty Optional is returned")
        @Test
        void findByNonExistingToken() {
            String token = "invalid-token";
            assertTrue(dao.findByToken(token).isEmpty());
        }
    }
}