package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.persistence.dao.jpa.AuthJpaDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}