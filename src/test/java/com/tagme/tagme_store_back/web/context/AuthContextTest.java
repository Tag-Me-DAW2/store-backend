package com.tagme.tagme_store_back.web.context;

import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthContext Tests")
class AuthContextTest {

    @AfterEach
    void tearDown() {
        AuthContext.clear();
    }

    @Nested
    @DisplayName("setUser and getUser Tests")
    class SetAndGetUserTests {

        @Test
        @DisplayName("Debería establecer y obtener usuario")
        void setUser_AndGetUser_ReturnsUser() {
            UserResponse user = new UserResponse(
                    1L, "testuser", "test@example.com",
                    "John", "Doe", "123456789", null, null, "CUSTOMER"
            );

            AuthContext.setUser(user);

            UserResponse result = AuthContext.getUser();

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("testuser", result.username());
            assertEquals("test@example.com", result.email());
        }

        @Test
        @DisplayName("Debería devolver null si no hay usuario establecido")
        void getUser_NoUserSet_ReturnsNull() {
            assertNull(AuthContext.getUser());
        }
    }

    @Nested
    @DisplayName("clear Tests")
    class ClearTests {

        @Test
        @DisplayName("Debería limpiar el contexto")
        void clear_RemovesUser() {
            UserResponse user = new UserResponse(
                    1L, "testuser", "test@example.com",
                    "John", "Doe", "123456789", null, null, "CUSTOMER"
            );

            AuthContext.setUser(user);
            assertNotNull(AuthContext.getUser());

            AuthContext.clear();

            assertNull(AuthContext.getUser());
        }
    }
}
