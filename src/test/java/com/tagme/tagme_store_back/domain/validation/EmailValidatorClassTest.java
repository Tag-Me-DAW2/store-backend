package com.tagme.tagme_store_back.domain.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmailValidatorClass Tests")
class EmailValidatorClassTest {

    private final EmailValidatorClass emailValidator = new EmailValidatorClass();

    @Nested
    @DisplayName("isValid Tests")
    class IsValidTests {

        @Test
        @DisplayName("Debería validar email correcto")
        void isValid_ValidEmail_ReturnsTrue() {
            assertTrue(emailValidator.isValid("test@example.com", null));
        }

        @Test
        @DisplayName("Debería validar email con subdominio")
        void isValid_EmailWithSubdomain_ReturnsTrue() {
            assertTrue(emailValidator.isValid("test@mail.example.com", null));
        }

        @Test
        @DisplayName("Debería validar email con números")
        void isValid_EmailWithNumbers_ReturnsTrue() {
            assertTrue(emailValidator.isValid("test123@example123.com", null));
        }

        @Test
        @DisplayName("Debería validar email con puntos en local part")
        void isValid_EmailWithDots_ReturnsTrue() {
            assertTrue(emailValidator.isValid("test.user.name@example.com", null));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Debería rechazar email null o vacío")
        void isValid_NullOrEmpty_ReturnsFalse(String email) {
            assertFalse(emailValidator.isValid(email, null));
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "invalid",
                "invalid@",
                "@example.com",
                "invalid@example",
                "invalid email@example.com",
                "invalid@@example.com"
        })
        @DisplayName("Debería rechazar emails inválidos")
        void isValid_InvalidEmails_ReturnsFalse(String email) {
            assertFalse(emailValidator.isValid(email, null));
        }
    }
}
