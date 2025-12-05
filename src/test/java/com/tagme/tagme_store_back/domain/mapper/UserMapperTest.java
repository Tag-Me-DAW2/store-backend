package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    @Nested
    class fromUserToUserDtoTests {
        @Test
        void fromUserToUserDto_NullInput_ReturnsNull() {
            assertNull(UserMapper.fromUserToUserDto(null));
        }

        @Test
        void fromUserToUserDto_ShouldMapCorrectly() {
            User user = Instancio.of(User.class)
                    .withSeed(10)
                    .create();

            UserDto userDto = UserMapper.fromUserToUserDto(user);

            assertNotNull(userDto);
            assertAll(
                    () -> assertEquals(user.getId(), userDto.id()),
                    () -> assertEquals(user.getUsername(), userDto.username()),
                    () -> assertEquals(user.getPassword(), userDto.password()),
                    () -> assertEquals(user.getEmail(), userDto.email()),
                    () -> assertEquals(user.getFirstName(), userDto.firstName()),
                    () -> assertEquals(user.getLastName(), userDto.lastName()),
                    () -> assertEquals(user.getPhone(), userDto.phone()),
                    () -> assertEquals(user.getRole(), userDto.role())
            );
        }
    }

    @Nested
    class fromUserDtoToUserTests {
        @Test
        void fromUserDtoToUser_NullInput_ReturnsNull() {
            assertNull(UserMapper.fromUserDtoToUser(null));
        }

        @Test
        void fromUserDtoToUser_ShouldMapCorrectly() {
            UserDto userDto = Instancio.of(UserDto.class)
                    .withSeed(10)
                    .create();

            User user = UserMapper.fromUserDtoToUser(userDto);

            assertNotNull(user);
            assertAll(
                    () -> assertEquals(user.getId(), userDto.id()),
                    () -> assertEquals(user.getUsername(), userDto.username()),
                    () -> assertEquals(user.getPassword(), userDto.password()),
                    () -> assertEquals(user.getEmail(), userDto.email()),
                    () -> assertEquals(user.getFirstName(), userDto.firstName()),
                    () -> assertEquals(user.getLastName(), userDto.lastName()),
                    () -> assertEquals(user.getPhone(), userDto.phone()),
                    () -> assertEquals(user.getRole(), userDto.role())
            );
        }
    }
}