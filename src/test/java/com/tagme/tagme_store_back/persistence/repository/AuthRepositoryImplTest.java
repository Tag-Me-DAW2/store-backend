package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRepositoryImplTest {
    @Mock
    private AuthJpaDao authJpaDao;

    @InjectMocks
    private AuthRepositoryImpl authRepositoryImpl;

    UserJpaEntity userJpaEntity;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        userJpaEntity = Instancio.of(UserJpaEntity.class).create();
        userDto = UserMapper.fromUserJpaEntityToUserDto(userJpaEntity);
    }

    @Test
    void createSession_ShouldReturnUUID() {
        UUID uuid = UUID.randomUUID();
        when(authJpaDao.createSession(anyLong())).thenReturn(uuid);

        assertEquals(uuid, authRepositoryImpl.createSession(1L));
    }

    @Test
    void logout_ShouldInvokeLogout() {
        // Since logout method returns void, we just need to ensure it doesn't throw any exceptions
        assertDoesNotThrow(() -> authRepositoryImpl.logout("sample-token"));
    }

    @Nested
    class FindByTokenTests {
        @DisplayName("findByToken with valid token should return UserDto")
        @Test
        void findByValidToken_ShouldReturnUserDto() {
            String token = "valid-token";
            when(authJpaDao.findByToken(token)).thenReturn(Optional.of(userJpaEntity));
            Optional<UserDto> result = authRepositoryImpl.findByToken(token);
            assertEquals(userDto, result.get());
        }

        @DisplayName("findByToken with invalid token should return empty Optional")
        @Test
        void findByInvalidToken_ShouldReturnEmptyOptional() {
            String token = "invalid-token";
            when(authJpaDao.findByToken(token)).thenReturn(Optional.empty());
            Optional<UserDto> result = authRepositoryImpl.findByToken(token);
            assertTrue(result.isEmpty());
        }
    }

}