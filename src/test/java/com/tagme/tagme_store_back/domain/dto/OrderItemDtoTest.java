package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemDtoTest {
    @Test
    void createOrderDto() {
        ProductDto productDto = Instancio.of(ProductDto.class).create();
        OrderItemDto orderItemDto = new OrderItemDto(1L, productDto, 2L, new BigDecimal(50L), new BigDecimal(10L), new BigDecimal(40L));

        assertDoesNotThrow(() -> DtoValidator.validate(orderItemDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("productDto", null),
                Arguments.of("quantity", null),
                Arguments.of("quantity", 0L),
                Arguments.of("basePrice", new BigDecimal(-1L)),
                Arguments.of("basePrice", null),
                Arguments.of("discountPercentage", null),
                Arguments.of("discountPercentage", new BigDecimal(-1L)),
                Arguments.of("discountPercentage", new BigDecimal(101L))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void categoryDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) throws SQLException {

        OrderItemDto orderItemDto= Instancio.of(OrderItemDto.class)
                .set(field(OrderItemDto.class, fieldName), invalidValue)
                .withSeed(10)
                .create();

        assertThrows(ValidationException.class, () -> DtoValidator.validate(orderItemDto));
    }

}
