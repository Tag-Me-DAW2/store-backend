package com.tagme.tagme_store_back.persistence.dao.jpa.impl;

import com.tagme.tagme_store_back.annotation.DaoTest;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.UserRole;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DaoTest
class UserJpaDaoImplTest extends BaseJpaDaoTest<UserJpaDao> {
    @Nested
    class FindByEmailTests {
        @DisplayName("Given an existing email, when findByEmail is called, then the corresponding user is returned")
        @Test
        void findByExistingEmail() {
            String email = "admin@example.com";
            UserJpaEntity expectedUser = dao.findByEmail(email).get();

            assertNotNull(expectedUser);
        }

        @DisplayName("Given a non-existing email, when findByEmail is called, then an empty Optional is returned")
        @Test
        void findByNonExistingEmail() {
            String email = "";
            assertTrue(dao.findByEmail(email).isEmpty());
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

    @Nested
    class FindByIdTests {
        @DisplayName("Given an existing user ID, when findById is called, then the corresponding user is returned")
        @Test
        void findByExistingId() {
            Long userId = 1L;
            UserJpaEntity expectedUser = dao.findById(userId).get();

            assertNotNull(expectedUser);
        }

        @DisplayName("Given a non-existing user ID, when findById is called, then an empty Optional is returned")
        @Test
        void findByNonExistingId() {
            Long userId = 999L;
            assertTrue(dao.findById(userId).isEmpty());
        }
    }

    @Nested
    class InsertTests {
        @DisplayName("Given a new user entity, when insert is called, then the user is persisted and returned")
        @Test
        void insertNewUser() {
            UserJpaEntity newUser = new UserJpaEntity();
            newUser.setEmail("prueba@gmail.com");
            newUser.setPassword("securepassword");
            newUser.setFirstName("prueba");
            newUser.setLastName("testing");
            newUser.setPhone("1234567890");
            newUser.setRole(UserRole.CUSTOMER);
            newUser.setUsername("prueba");
            UserJpaEntity insertedUser = dao.insert(newUser);
            assertNotNull(insertedUser.getId());
        }
    }

    @Nested
    class UpdateTests {
        @DisplayName("Given an existing user entity, when update is called, then the user is updated and returned")
        @Test
        void updateExistingUser() {
            Long userId = 1L;
            UserJpaEntity userToUpdate = dao.findById(userId).get();
            String newFirstName = "UpdatedName";
            userToUpdate.setFirstName(newFirstName);
            UserJpaEntity updatedUser = dao.update(userToUpdate);
            assertEquals(newFirstName, updatedUser.getFirstName());
        }

        @DisplayName("Given a non-existing user entity, when update is called, then a ResourceNotFoundException is thrown")
        @Test
        void updateNonExistingUser() {
            Long userId = 1L;
            UserJpaEntity userToUpdate = dao.findById(userId).get();
            userToUpdate.setId(999L);

            assertThrows(ResourceNotFoundException.class, () -> dao.update(userToUpdate));
        }

        @DisplayName("Password update is not possible")
        @Test
        void passwordUpdateNotPossible() {
            Long userId = 1L;

            UserJpaEntity managed = dao.findById(userId).get();
            String originalPassword = managed.getPassword();

            UserJpaEntity detached = new UserJpaEntity();
            detached.setId(managed.getId());
            detached.setUsername(managed.getUsername());
            detached.setEmail(managed.getEmail());
            detached.setPassword("newpassword");

            dao.update(detached);

            UserJpaEntity reloaded = dao.findById(userId).get();

            assertEquals(originalPassword, reloaded.getPassword());
        }
    }
}