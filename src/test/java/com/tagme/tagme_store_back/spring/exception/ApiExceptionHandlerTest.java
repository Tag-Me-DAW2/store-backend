package com.tagme.tagme_store_back.spring.exception;

import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiExceptionHandler Tests")
class ApiExceptionHandlerTest {

    private ApiExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ApiExceptionHandler();
    }

    @Nested
    @DisplayName("handleResourceNotFoundException Tests")
    class HandleResourceNotFoundExceptionTests {

        @Test
        @DisplayName("Debería manejar ResourceNotFoundException")
        void handleResourceNotFoundException_ReturnsErrorMessage() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Recurso no encontrado");

            ErrorMessage result = exceptionHandler.handleResourceNotFoundException(ex);

            assertNotNull(result);
            assertEquals("Recurso no encontrado", result.getMessage());
        }
    }

    @Nested
    @DisplayName("handleValidationException Tests")
    class HandleValidationExceptionTests {

        @Test
        @DisplayName("Debería manejar ValidationException")
        void handleValidationException_ValidationException_ReturnsErrorMessage() {
            ValidationException ex = new ValidationException(new HashSet<>());

            ErrorMessage result = exceptionHandler.handleValidationException(ex);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Debería manejar IllegalArgumentException")
        void handleValidationException_IllegalArgumentException_ReturnsErrorMessage() {
            IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

            ErrorMessage result = exceptionHandler.handleValidationException(ex);

            assertNotNull(result);
            assertEquals("Argumento inválido", result.getMessage());
        }

        @Test
        @DisplayName("Debería manejar BusinessException")
        void handleValidationException_BusinessException_ReturnsErrorMessage() {
            BusinessException ex = new BusinessException("Error de negocio");

            ErrorMessage result = exceptionHandler.handleValidationException(ex);

            assertNotNull(result);
            assertEquals("Error de negocio", result.getMessage());
        }
    }

    @Nested
    @DisplayName("handleInvalidCredentialsException Tests")
    class HandleInvalidCredentialsExceptionTests {

        @Test
        @DisplayName("Debería manejar InvalidCredentialsException")
        void handleInvalidCredentialsException_ReturnsErrorMessage() {
            InvalidCredentialsException ex = new InvalidCredentialsException("Credenciales inválidas");

            ErrorMessage result = exceptionHandler.handleInvalidCredentialsException(ex);

            assertNotNull(result);
            assertEquals("Credenciales inválidas", result.getMessage());
        }
    }

    @Nested
    @DisplayName("handleRuntimeException Tests")
    class HandleRuntimeExceptionTests {

        @Test
        @DisplayName("Debería manejar RuntimeException")
        void handleRuntimeException_ReturnsErrorMessage() {
            RuntimeException ex = new RuntimeException("Error de runtime");

            ErrorMessage result = exceptionHandler.handleRuntimeException(ex);

            assertNotNull(result);
            assertEquals("Error de runtime", result.getMessage());
        }
    }

    @Nested
    @DisplayName("handleGeneralException Tests")
    class HandleGeneralExceptionTests {

        @Test
        @DisplayName("Debería manejar Exception genérica")
        void handleGeneralException_ReturnsErrorMessage() {
            Exception ex = new Exception("Error general");

            ErrorMessage result = exceptionHandler.handleGeneralException(ex);

            assertNotNull(result);
            assertEquals("Error general", result.getMessage());
        }
    }
}
