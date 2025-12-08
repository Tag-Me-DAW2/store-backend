package com.tagme.tagme_store_back.domain.dto;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {
    Model<UserDto> userDtoModel;

    @BeforeEach
    void setup() {
        userDtoModel = Instancio.of(UserDto.class)
                .set(field(UserDto::email), "user@gmail.com")
                .set(field(UserDto::phone), "+34612345678")
                .withSeed(10)
                .toModel();
    }

    @Test
    void userDto_Creation_ShouldSucceed() throws SQLException {
        UserDto userDto = Instancio.of(UserDto.class)
                .set(field("email"), "pepe@example.com")
                .set(field("phone"), "+34612345678")
                .withSeed(10)
                .create();

        assertDoesNotThrow(() -> DtoValidator.validate(userDto));
    }

    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("username", null),
                Arguments.of("username", ""),
                Arguments.of("username", " "),

                Arguments.of("password", null),
                Arguments.of("password", ""),
                Arguments.of("password", " "),

                Arguments.of("email", null),
                Arguments.of("email", ""),
                Arguments.of("email", " "),
                Arguments.of("email", "user..name@gmail.com"),
                Arguments.of("email", "username.@gmail.com"),
                Arguments.of("email", "user@-domain.com"),
                Arguments.of("email", "user@domain-.com"),
                Arguments.of("email", "user@.com"),
                Arguments.of("email", "user name@gmail.com"),
                Arguments.of("email", "user@domain .com"),
                Arguments.of("email", "user@dom@in.com"),
                Arguments.of("email", ".user@dom@in.com"),
                Arguments.of("email", ".user@domin.com"),
                Arguments.of("email", "@"),

                Arguments.of("firstName", null),
                Arguments.of("firstName", ""),
                Arguments.of("firstName", " "),

                Arguments.of("phone", " "),
                Arguments.of("phone", "invalid phone"),
                Arguments.of("phone", "12345"),
                Arguments.of("phone", "0140123456789"),
                Arguments.of("phone", "+346905074450793112412"),

                Arguments.of("role", null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void userDto_WithInvalidUsername_ShouldFailValidation(String fieldName, Object invalidValue) {


        UserDto userDto = Instancio.of(userDtoModel)
                .set(Select.field(fieldName), invalidValue)
                .create();

        System.out.println(userDto);
        assertThrows(ValidationException.class, () -> DtoValidator.validate(userDto));
    }
}