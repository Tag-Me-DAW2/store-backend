package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginDtoTest {
    @Test
    void loginDto_Creation_ShouldSucceed() throws SQLException {
        LoginDto loginDto = Instancio.of(LoginDto.class)
                .withSeed(10)
                .create();

        assertDoesNotThrow(() -> DtoValidator.validate(loginDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("email", null),
                Arguments.of("email", ""),
                Arguments.of("password", ""),
                Arguments.of("password", null));
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void loginDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) throws SQLException {

        LoginDto loginDto = Instancio.of(LoginDto.class)
                .set(field(LoginDto.class, fieldName), invalidValue)
                .withSeed(10)
                .create();

        assertThrows(ValidationException.class, () -> DtoValidator.validate(loginDto));
    }
}
