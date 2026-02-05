package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.model.OrderStatus;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDtoTest {
    @Test
    void createOrderDto_Success() {
        UserDto user = Instancio.of(UserDto.class).create();
        List<OrderItemDto> orderItems = Instancio.ofList(OrderItemDto.class).size(3).create();
        OrderDto orderDto = new OrderDto(null, user, OrderStatus.PENDING, orderItems, null, null, null, null, null);

        assertDoesNotThrow(() -> DtoValidator.validate(orderDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("user", null),
                Arguments.of("orderStatus", null),
                Arguments.of("totalPrice", new BigDecimal(-1L))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void categoryDto_WithInvalidData_ShouldFailValidation(String fieldName, Object invalidValue) throws SQLException {

        OrderDto orderDto= Instancio.of(OrderDto.class)
                .set(field(OrderDto.class, fieldName), invalidValue)
                .withSeed(10)
                .create();

        assertThrows(ValidationException.class, () -> DtoValidator.validate(orderDto));
    }
}
