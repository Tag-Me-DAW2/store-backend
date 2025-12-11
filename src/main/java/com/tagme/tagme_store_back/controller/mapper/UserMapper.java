package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.UserInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.UserUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.UserRole;

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

    public static UserDto fromUserInsertRequestToUserDto(UserInsertRequest request) {
        if (request == null) {
            return null;
        }

        return new UserDto(
                null,
                request.username(),
                request.password(),
                request.email(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                UserRole.valueOf(request.role())
        );
    }

    public static UserDto fromUserUpdateRequestToUserDto(UserUpdateRequest request) {
        if (request == null) {
            return null;
        }

        return new UserDto(
                request.id(),
                request.username(),
                null,
                request.email(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                UserRole.valueOf(request.role())
        );
    }

}
