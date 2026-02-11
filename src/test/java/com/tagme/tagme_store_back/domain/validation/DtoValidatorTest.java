package com.tagme.tagme_store_back.domain.validation;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.ProductDto;
import com.tagme.tagme_store_back.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DtoValidator Tests")
class DtoValidatorTest {

    @Nested
    @DisplayName("validate Tests")
    class ValidateTests {

        @Test
        @DisplayName("Debería validar DTO correcto sin excepción")
        void validate_ValidDto_NoException() {
            // CategoryDto tiene solo id y name
            CategoryDto validCategory = new CategoryDto(1L, "Valid Category");

            assertDoesNotThrow(() -> DtoValidator.validate(validCategory));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException para DTO inválido")
        void validate_InvalidDto_ThrowsValidationException() {
            // CategoryDto con nombre null
            CategoryDto invalidCategory = new CategoryDto(1L, null);

            assertThrows(ValidationException.class, () -> DtoValidator.validate(invalidCategory));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException con múltiples violaciones")
        void validate_MultipleViolations_ThrowsValidationException() {
            // ProductDto con múltiples errores
            ProductDto invalidProduct = new ProductDto(
                    1L,
                    null, // name null
                    null, // description null
                    BigDecimal.valueOf(-10), // precio negativo
                    null, // discountPercentage null
                    null,
                    null, // image null
                    null, // imageName null
                    null, // category null
                    null
            );

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> DtoValidator.validate(invalidProduct)
            );

            assertNotNull(exception.getMessage());
        }
    }
}
