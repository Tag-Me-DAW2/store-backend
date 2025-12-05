package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class ProductDtoTest {
    @Test
    void productDto_Creation_ShouldSucceed() throws SQLException {
        byte[] imageData = "test image data".getBytes();
        Blob testBlob = new SerialBlob(imageData);
        ProductDto productDto = Instancio.of(ProductDto.class)
                .set(field(ProductDto.class, "image"), testBlob)
                .withSeed(10)
                .create();

        assertDoesNotThrow(() -> DtoValidator.validate(productDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("name", null),
                Arguments.of("name", ""),
                Arguments.of("name", " "),
                Arguments.of("description", null),
                Arguments.of("description", ""),
                Arguments.of("description", " "),

                Arguments.of("basePrice", new BigDecimal("-1.0")),
                Arguments.of("basePrice", new BigDecimal("0.0")),

                Arguments.of("basePrice", new BigDecimal("0.0")),
                Arguments.of("discountPercentage", new BigDecimal("-5.0")),
                Arguments.of("discountPercentage", new BigDecimal("150.0")),

                Arguments.of("category", null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void productDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) throws SQLException {
        byte[] imageData = "test image data".getBytes();
        Blob testBlob = new SerialBlob(imageData);

        ProductDto productDto = Instancio.of(ProductDto.class)
                .set(field(ProductDto.class, fieldName), invalidValue)
                .set(field(ProductDto.class, "image"), testBlob)
                .withSeed(10)
                .create();

        assertThrows(ValidationException.class, () -> DtoValidator.validate(productDto));
    }
}