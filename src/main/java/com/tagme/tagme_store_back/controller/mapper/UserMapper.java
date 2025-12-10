package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.UserDto;

public class UserMapper {
    public static UserResponse fromUserDtoToUserResponse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return new UserResponse(
                userDto.id(),
                userDto.username(),
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.phone(),
                userDto.role().name()
        );
    }

}
