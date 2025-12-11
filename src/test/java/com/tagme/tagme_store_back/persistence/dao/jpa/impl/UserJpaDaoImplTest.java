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

    @Nested
    class DeleteByIdTests {
        @DisplayName("Given an existing user ID, when deleteById is called, then the user is deleted")
        @Test
        void deleteByExistingId() {
            Long userId = 1L;
            Long countBeforeDelete = dao.count();
            dao.deleteById(userId);
            Long countAfterDelete = dao.count();
            assertAll(
                    () -> assertEquals(countBeforeDelete - 1, countAfterDelete),
                    () -> assertTrue(dao.findById(userId).isEmpty())
            );
        }

        @DisplayName("Given a non-existing user ID, when deleteById is called, should throw ResourceNotFoundException")
        @Test
        void deleteByNonExistingId() {
            Long userId = 999L;
            assertThrows(ResourceNotFoundException.class, () -> dao.deleteById(userId));
        }

        @DisplayName("Given a null user ID, when deleteById is called, then should throw RuntimeException")
        @Test
        void deleteByNullId() {
            Long userId = null;
            assertThrows(RuntimeException.class, () -> dao.deleteById(userId));
        }
    }
}