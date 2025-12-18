package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.User;

public class UserMapper {
    public static UserDto fromUserToUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getProfilePicture(),
                user.getProfilePictureName(),
                user.getRole()
        );
    }

    public static  User fromUserDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return new User(
                userDto.id(),
                userDto.username(),
                userDto.password(),
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.phone(),
                userDto.profilePicture(),
                userDto.profilePictureName(),
                userDto.role()
        );
    }
}
