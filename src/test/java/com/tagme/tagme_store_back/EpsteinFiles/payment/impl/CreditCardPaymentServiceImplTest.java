package com.tagme.tagme_store_back.EpsteinFiles.payment.impl;

import com.tagme.tagme_store_back.EpsteinFiles.http.HttpClientService;
import com.tagme.tagme_store_back.EpsteinFiles.http.exception.ApiNotWorkingException;
import com.tagme.tagme_store_back.EpsteinFiles.http.response.ApiResponse;
import com.tagme.tagme_store_back.EpsteinFiles.payment.records.CreditCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreditCardPaymentService Tests")
class CreditCardPaymentServiceImplTest {

    @Mock
    private HttpClientService httpClient;

    @InjectMocks
    private CreditCardPaymentServiceImpl creditCardPaymentService;

    private CreditCardRequest creditCardRequest;
    private BigDecimal amount;

    @BeforeEach
    void setUp() {
        // Set up the @Value fields using ReflectionTestUtils
        ReflectionTestUtils.setField(creditCardPaymentService, "bankApiHost", "http://localhost:8081");
        ReflectionTestUtils.setField(creditCardPaymentService, "bankApiKey", "test-api-key");
        ReflectionTestUtils.setField(creditCardPaymentService, "bankApiUsername", "testuser");
        ReflectionTestUtils.setField(creditCardPaymentService, "bankApiIban", "ES9121000418450200051332");

        creditCardRequest = new CreditCardRequest(
                "4111111111111111",
                "12/25",
                "123",
                "Test User"
        );
        amount = BigDecimal.valueOf(100);
    }

    @Nested
    @DisplayName("execute Tests")
    class ExecuteTests {

        @Test
        @DisplayName("Given valid payment data, when execute is called, then payment succeeds")
        void executeSuccess() {
            // Arrange
            ApiResponse successResponse = new ApiResponse(200, "Payment successful");
            when(httpClient.post(anyString(), any())).thenReturn(successResponse);

            // Act & Assert
            assertDoesNotThrow(() -> creditCardPaymentService.execute(creditCardRequest, amount));

            verify(httpClient).post(eq("http://localhost:8081/payments/credit-card"), any());
        }

        @Test
        @DisplayName("Given API returns 500 error, when execute is called, then throw ApiNotWorkingException")
        void executeApiNotWorking500() {
            // Arrange
            ApiResponse errorResponse = new ApiResponse(500, "Internal Server Error");
            when(httpClient.post(anyString(), any())).thenReturn(errorResponse);

            // Act & Assert
            ApiNotWorkingException exception = assertThrows(
                    ApiNotWorkingException.class,
                    () -> creditCardPaymentService.execute(creditCardRequest, amount)
            );

            assertEquals("Bank API is currently unavailable. Please try again later.", exception.getMessage());
        }

        @Test
        @DisplayName("Given API returns 503 error, when execute is called, then throw ApiNotWorkingException")
        void executeApiNotWorking503() {
            // Arrange
            ApiResponse errorResponse = new ApiResponse(503, "Service Unavailable");
            when(httpClient.post(anyString(), any())).thenReturn(errorResponse);

            // Act & Assert
            ApiNotWorkingException exception = assertThrows(
                    ApiNotWorkingException.class,
                    () -> creditCardPaymentService.execute(creditCardRequest, amount)
            );

            assertEquals("Bank API is currently unavailable. Please try again later.", exception.getMessage());
        }

        @Test
        @DisplayName("Given API returns 400 error, when execute is called, then throw RuntimeException with body")
        void executeBadRequest() {
            // Arrange
            String errorBody = "Invalid credit card number";
            ApiResponse errorResponse = new ApiResponse(400, errorBody);
            when(httpClient.post(anyString(), any())).thenReturn(errorResponse);

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> creditCardPaymentService.execute(creditCardRequest, amount)
            );

            assertEquals(errorBody, exception.getMessage());
        }

        @Test
        @DisplayName("Given API returns 401 error, when execute is called, then throw RuntimeException with body")
        void executeUnauthorized() {
            // Arrange
            String errorBody = "Unauthorized access";
            ApiResponse errorResponse = new ApiResponse(401, errorBody);
            when(httpClient.post(anyString(), any())).thenReturn(errorResponse);

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> creditCardPaymentService.execute(creditCardRequest, amount)
            );

            assertEquals(errorBody, exception.getMessage());
        }

        @Test
        @DisplayName("Given API returns 404 error, when execute is called, then throw RuntimeException with body")
        void executeNotFound() {
            // Arrange
            String errorBody = "Credit card not found";
            ApiResponse errorResponse = new ApiResponse(404, errorBody);
            when(httpClient.post(anyString(), any())).thenReturn(errorResponse);

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> creditCardPaymentService.execute(creditCardRequest, amount)
            );

            assertEquals(errorBody, exception.getMessage());
        }

        @Test
        @DisplayName("Given valid payment with different amount, when execute is called, then correct amount is sent")
        void executeWithDifferentAmount() {
            // Arrange
            BigDecimal largeAmount = BigDecimal.valueOf(999.99);
            ApiResponse successResponse = new ApiResponse(200, "Payment successful");
            when(httpClient.post(anyString(), any())).thenReturn(successResponse);

            // Act & Assert
            assertDoesNotThrow(() -> creditCardPaymentService.execute(creditCardRequest, largeAmount));

            verify(httpClient).post(eq("http://localhost:8081/payments/credit-card"), any());
        }

        @Test
        @DisplayName("Given API returns 201 status, when execute is called, then payment succeeds")
        void executeSuccess201() {
            // Arrange
            ApiResponse successResponse = new ApiResponse(201, "Created");
            when(httpClient.post(anyString(), any())).thenReturn(successResponse);

            // Act & Assert
            assertDoesNotThrow(() -> creditCardPaymentService.execute(creditCardRequest, amount));
        }

        @Test
        @DisplayName("Given API returns 204 status, when execute is called, then payment succeeds")
        void executeSuccess204() {
            // Arrange
            ApiResponse successResponse = new ApiResponse(204, "");
            when(httpClient.post(anyString(), any())).thenReturn(successResponse);

            // Act & Assert
            assertDoesNotThrow(() -> creditCardPaymentService.execute(creditCardRequest, amount));
        }

        @Test
        @DisplayName("Given API returns 499 client error, when execute is called, then throw RuntimeException")
        void executeClientError499() {
            // Arrange
            String errorBody = "Client error";
            ApiResponse errorResponse = new ApiResponse(499, errorBody);
            when(httpClient.post(anyString(), any())).thenReturn(errorResponse);

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> creditCardPaymentService.execute(creditCardRequest, amount)
            );

            assertEquals(errorBody, exception.getMessage());
        }
    }
}
