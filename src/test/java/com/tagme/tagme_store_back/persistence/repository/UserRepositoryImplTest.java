package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import com.tagme.tagme_store_back.persistence.mapper.UserMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {
    @Mock
    private UserJpaDao userJpaDao;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    UserJpaEntity userJpaEntity;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        userJpaEntity = Instancio.of(UserJpaEntity.class).create();
        userDto = UserMapper.fromUserJpaEntityToUserDto(userJpaEntity);
    }

    @Nested
    class findByIdTests {
        @DisplayName("findById with valid ID should return UserDto")
        @Test
        void findByValidId_ShouldReturnUserDto() {
            when(userJpaDao.findById(anyLong())).thenReturn(Optional.of(userJpaEntity));

            Optional<UserDto> result = userRepositoryImpl.findById(userJpaEntity.getId());

            assertEquals(userDto, result.get());
        }

        @DisplayName("findById with invalid ID should return empty Optional")
        @Test
        void findByInvalidId_ShouldReturnEmptyOptional() {
            when(userJpaDao.findById(anyLong())).thenReturn(Optional.empty());
            Optional<UserDto> result = userRepositoryImpl.findById(999L);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class SaveUserTests {
        @DisplayName("save should create and return UserDto when ID is null")
        @Test
        void save_ShouldCreateAndReturnUserDto_WhenIdIsNull() {
            UserDto newUserDto = Instancio.of(UserDto.class)
                    .set(field("id"), null)
                    .create();
            UserJpaEntity savedUserJpaEntity = Instancio.of(UserJpaEntity.class)
                    .set(field("id"), 1L)
                    .create();

            when(userJpaDao.insert(any(UserJpaEntity.class))).thenReturn(savedUserJpaEntity);
            UserDto result = userRepositoryImpl.save(newUserDto);

            assertEquals(UserMapper.fromUserJpaEntityToUserDto(savedUserJpaEntity), result);
        }

        @DisplayName("save should update and return UserDto when ID is not null")
        @Test
        void save_ShouldUpdateAndReturnUserDto_WhenIdIsNotNull() {
            when(userJpaDao.update(any(UserJpaEntity.class))).thenReturn(userJpaEntity);
            UserDto result = userRepositoryImpl.save(userDto);
            assertEquals(userDto, result);
        }
    }

    @Nested
    class DeleteByIdTests {
        @DisplayName("deleteById should call userJpaDao.deleteById with correct ID")
        @Test
        void deleteById_ShouldCallUserJpaDaoDeleteById_WithCorrectId() {
            Long userId = 1L;

            userRepositoryImpl.deleteById(userId);

            verify(userJpaDao).deleteById(userId);
        }
    }

    @Nested
    class FindByEmailTests {
        @DisplayName("findByEmail with valid email should return UserDto")
        @Test
        void findByValidEmail_ShouldReturnUserDto() {
            String email = "example@gmail.com";
            when(userJpaDao.findByEmail(email)).thenReturn(Optional.of(userJpaEntity));
            Optional<UserDto> result = userRepositoryImpl.findByEmail(email);
            assertEquals(userDto, result.get());
        }

        @DisplayName("findByEmail with invalid email should return empty Optional")
        @Test
        void findByInvalidEmail_ShouldReturnEmptyOptional() {
            String email = "";
            when(userJpaDao.findByEmail(email)).thenReturn(Optional.empty());
            Optional<UserDto> result = userRepositoryImpl.findByEmail(email);
            assertTrue(result.isEmpty());
        }
    }
}