package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemDtoTest {

    private OrderItemDto createValidOrderItem() {
        return new OrderItemDto(
                1L,
                null, // productDto
                "Test Product",
                null, // productImage
                "test.jpg",
                2L,
                new BigDecimal("50.00"),
                new BigDecimal("10.00"),
                new BigDecimal("90.00")
        );
    }

    @Test
    void createOrderDto() {
        OrderItemDto orderItemDto = createValidOrderItem();
        assertDoesNotThrow(() -> DtoValidator.validate(orderItemDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                // quantity con valor 0 - violación de @Min(1)
                Arguments.of("quantity", 0L),
                // basePrice negativo - violación de @Min(0)
                Arguments.of("basePrice", new BigDecimal("-1.00")),
                // discountPercentage negativo - violación de @DecimalMin(0)
                Arguments.of("discountPercentage", new BigDecimal("-1.00")),
                // discountPercentage mayor a 100 - violación de @DecimalMax(100)
                Arguments.of("discountPercentage", new BigDecimal("101.00"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void categoryDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) throws SQLException {
        OrderItemDto orderItemDto;
        
        switch (fieldName) {
            case "quantity":
                orderItemDto = new OrderItemDto(1L, null, "Test", null, "test.jpg",
                        (Long) invalidValue, BigDecimal.valueOf(50), BigDecimal.valueOf(10), BigDecimal.valueOf(90));
                break;
            case "basePrice":
                orderItemDto = new OrderItemDto(1L, null, "Test", null, "test.jpg",
                        2L, (BigDecimal) invalidValue, BigDecimal.valueOf(10), BigDecimal.valueOf(90));
                break;
            case "discountPercentage":
                orderItemDto = new OrderItemDto(1L, null, "Test", null, "test.jpg",
                        2L, BigDecimal.valueOf(50), (BigDecimal) invalidValue, BigDecimal.valueOf(90));
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + fieldName);
        }

        assertThrows(ValidationException.class, () -> DtoValidator.validate(orderItemDto));
    }
}
