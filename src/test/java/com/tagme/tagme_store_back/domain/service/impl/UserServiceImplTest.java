package com.tagme.tagme_store_back.domain.service.impl;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private  UserServiceImpl userService;

    private UserDto userDto1;

    @BeforeEach
    void setUp() {
        userDto1 = Instancio.of(UserDto.class).create();
    }

    @Nested
    class GetAllTests {
        @DisplayName("Given valid page and size, when getAll is called, then return the paged result")
        @Test
        void testGetAllValidPagination() {
            Page<UserDto> repoPage = new Page<>(List.of(userDto1), 1, 1, 1L);

            when(userRepository.findAll(anyInt(), anyInt()))
                    .thenReturn(repoPage);

            var result = userService.getAll(1, 1);

            assertNotNull(result);
            assertEquals(1, result.data().size());
            assertEquals(userDto1.id(), result.data().get(0).id());
            assertEquals(1, result.pageNumber());
            assertEquals(1, result.pageSize());
            assertEquals(1L, result.totalElements());
        }

        @DisplayName("Given invalid page or size, when getAll is called, then throw BusinessException")
        @Test
        void testGetAllInvalidParams() {
            assertThrows(BusinessException.class, () -> userService.getAll(0, 1));
            assertThrows(BusinessException.class, () -> userService.getAll(1, 0));
            assertThrows(BusinessException.class, () -> userService.getAll(-1, -5));
        }
    }
    @Nested
    class GetByIdTests {
        @DisplayName("Given existing user ID, when getById is called, then return the corresponding UserDto")
        @Test
        void testGetByIdExistingUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDto1));

            UserDto actualUser = userService.getById(userDto1.id());

            assertEquals(userDto1, actualUser);
        }

        @DisplayName("Given non-existing user ID, when getById is called, then throw ResourceNotFoundException")
        @Test
        void testGetByIdNonExistingUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> userService.getById(9999999L));
        }

        @DisplayName("Given null user ID, when getById is called, then throw RuntimeException")
        @Test
        void testGetByIdNullId() {
            assertThrows(RuntimeException.class, () -> userService.getById(null));
        }
    }

    @Nested
    class CreateUserTests {
        @DisplayName("Given unique email, when create is called, then return the saved UserDto")
        @Test
        void testCreateUniqueEmail() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            when(userRepository.save(any())).thenReturn(userDto1);

            UserDto createdUser = userService.create(userDto1);

            assertEquals(userDto1, createdUser);
        }

        @DisplayName("Given existing email, when create is called, then throw BusinessException")
        @Test
        void testCreateExistingEmail() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(userDto1));

            assertThrows(BusinessException.class, () -> userService.create(userDto1));
        }

        @DisplayName("Given empty password, when create is called, then throw BusinessException")
        @Test
        void testCreateEmptyPassword() {
            UserDto userWithEmptyPassword = Instancio.of(UserDto.class)
                    .set(field(UserDto::password), "")
                    .create();
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> userService.create(userWithEmptyPassword));
        }
    }

    @Nested
    class UpdateUserTests {
        @DisplayName("Given existing user ID and unique email, when update is called, then return the updated UserDto")
        @Test
        void testUpdateExistingUserUniqueEmail() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDto1));
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(userDto1));
            when(userRepository.save(any())).thenReturn(userDto1);

            UserDto updatedUser = userService.update(userDto1);

            assertEquals(userDto1, updatedUser);
        }

        @DisplayName("Given non-existing user ID, when update is called, then throw ResourceNotFoundException")
        @Test
        void testUpdateNonExistingUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userService.update(userDto1));
        }

        @DisplayName("Given existing email used by another user, when update is called, then throw BusinessException")
        @Test

        void testUpdateExistingEmailUsedByAnotherUser() {
            UserDto anotherUser = Instancio.of(UserDto.class).create();
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDto1));
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(anotherUser));

            assertThrows(BusinessException.class, () -> userService.update(userDto1));
        }
    }

    @Nested
    class DeleteUserTests {
        @DisplayName("Given existing user ID, when delete is called, then delete the user successfully")
        @Test
        void testDeleteExistingUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(userDto1));

            assertDoesNotThrow(() -> userService.deleteById(userDto1.id()));
        }

        @DisplayName("Given non-existing user ID, when delete is called, then throw ResourceNotFoundException")
        @Test
        void testDeleteNonExistingUser() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(9999999L));
        }

        @DisplayName("Given null user ID, when delete is called, then throw RuntimeException")
        @Test
        void testDeleteNullId() {
            assertThrows(RuntimeException.class, () -> userService.deleteById(null));
        }
    }
}