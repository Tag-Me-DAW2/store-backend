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
    class FindAllTests {
        @DisplayName("Given valid page and size, when findAll is called, then return the expected page")
        @Test
        void findAllValidPagination() {
            int page = 1;
            int size = 1;

            var result = dao.findAll(page, size);

            assertNotNull(result);
            assertEquals(size, result.size());
            assertEquals(1L, result.get(0).getId());
        }

        @DisplayName("Given page 0, when findAll is called, then it is treated as first page")
        @Test
        void findAllPageZeroTreatedAsFirst() {
            var pageZero = dao.findAll(0, 1);
            var firstPage = dao.findAll(1, 1);

            assertEquals(firstPage.size(), pageZero.size());
            if (!firstPage.isEmpty()) {
                assertEquals(firstPage.get(0).getId(), pageZero.get(0).getId());
            }
        }

        @DisplayName("Given size 0, when findAll is called, then return an empty list")
        @Test
        void findAllSizeZeroReturnsEmpty() {
            var result = dao.findAll(1, 0);
            assertTrue(result.isEmpty());
        }

        @DisplayName("Given a very large size, when findAll is called, then return all seeded rows")
        @Test
        void findAllLargeSizeReturnsAll() {
            int total = Math.toIntExact(dao.count());
            var result = dao.findAll(1, total + 10);

            assertEquals(total, result.size());
        }
    }

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

    @Nested
    class UpdatePasswordTests {
        @DisplayName("Given an existing user, when updatePassword is called, then the password is updated")
        @Test
        void updatePasswordExistingUser() {
            Long userId = 1L;
            UserJpaEntity user = dao.findById(userId).get();
            String oldPassword = user.getPassword();
            
            UserJpaEntity userToUpdate = new UserJpaEntity();
            userToUpdate.setId(userId);
            userToUpdate.setPassword("newSecurePassword123");
            
            dao.updatePassword(userToUpdate);
            
            UserJpaEntity reloaded = dao.findById(userId).get();
            assertEquals("newSecurePassword123", reloaded.getPassword());
            assertNotEquals(oldPassword, reloaded.getPassword());
        }

        @DisplayName("Given a non-existing user, when updatePassword is called, then throw ResourceNotFoundException")
        @Test
        void updatePasswordNonExistingUser() {
            UserJpaEntity userToUpdate = new UserJpaEntity();
            userToUpdate.setId(999L);
            userToUpdate.setPassword("newPassword");
            
            assertThrows(ResourceNotFoundException.class, () -> dao.updatePassword(userToUpdate));
        }
    }

    @Nested
    class CountTests {
        @DisplayName("When count is called, then return total number of users")
        @Test
        void countReturnsTotal() {
            Long count = dao.count();
            assertNotNull(count);
            assertTrue(count > 0);
        }
    }
}