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
import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoTest {

    @Test
    void categoryDto_Creation_ShouldSucceed() throws SQLException {
        CategoryDto categoryDto = Instancio.of(CategoryDto.class)
                .withSeed(10)
                .create();

        assertDoesNotThrow(() -> DtoValidator.validate(categoryDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("name", null),
                Arguments.of("name", ""),
                Arguments.of("name", " "));
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void categoryDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) throws SQLException {

        CategoryDto categoryDto = Instancio.of(CategoryDto.class)
                .set(field(CategoryDto.class, fieldName), invalidValue)
                .withSeed(10)
                .create();

        assertThrows(ValidationException.class, () -> DtoValidator.validate(categoryDto));
    }
}