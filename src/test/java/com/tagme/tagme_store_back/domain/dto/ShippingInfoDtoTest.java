package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ShippingInfoDto Tests")
class ShippingInfoDtoTest {

    @Test
    @DisplayName("Debería crear ShippingInfoDto válido")
    void createShippingInfoDto_Success() {
        ShippingInfoDto shippingInfoDto = new ShippingInfoDto(
                1L,
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main Street",
                "Madrid",
                "28001",
                "Spain",
                LocalDateTime.now()
        );

        assertDoesNotThrow(() -> DtoValidator.validate(shippingInfoDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("firstName", null),
                Arguments.of("firstName", ""),
                Arguments.of("firstName", "   "),
                Arguments.of("lastName", null),
                Arguments.of("lastName", ""),
                Arguments.of("lastName", "   "),
                Arguments.of("email", null),
                Arguments.of("email", ""),
                Arguments.of("email", "invalid-email"),
                Arguments.of("address", null),
                Arguments.of("address", ""),
                Arguments.of("address", "   "),
                Arguments.of("city", null),
                Arguments.of("city", ""),
                Arguments.of("city", "   "),
                Arguments.of("postalCode", null),
                Arguments.of("postalCode", ""),
                Arguments.of("postalCode", "   "),
                Arguments.of("country", null),
                Arguments.of("country", ""),
                Arguments.of("country", "   ")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    @DisplayName("Debería fallar validación con datos inválidos")
    void shippingInfoDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) {
        ShippingInfoDto shippingInfoDto = Instancio.of(ShippingInfoDto.class)
                .set(field(ShippingInfoDto.class, fieldName), invalidValue)
                .set(field(ShippingInfoDto.class, "email"), "valid@email.com")
                .withSeed(10)
                .create();

        if (fieldName.equals("email")) {
            shippingInfoDto = Instancio.of(ShippingInfoDto.class)
                    .set(field(ShippingInfoDto.class, "email"), invalidValue)
                    .withSeed(10)
                    .create();
        }

        ShippingInfoDto finalDto = shippingInfoDto;
        assertThrows(ValidationException.class, () -> DtoValidator.validate(finalDto));
    }
}
