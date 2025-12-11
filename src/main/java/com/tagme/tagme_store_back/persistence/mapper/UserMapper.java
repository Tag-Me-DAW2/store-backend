package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.User;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;

import java.util.Objects;

public class UserMapper {
    public static UserDto fromUserJpaEntityToUserDto(UserJpaEntity userJpaEntity) {
        if(userJpaEntity == null) {
            return null;
        }

        return new UserDto(
                userJpaEntity.getId(),
                userJpaEntity.getUsername(),
                userJpaEntity.getPassword(),
                userJpaEntity.getEmail(),
                userJpaEntity.getFirstName(),
                userJpaEntity.getLastName(),
                userJpaEntity.getPhone(),
                userJpaEntity.getRole()
        );
    }

    public static UserJpaEntity fromUserDtoToUserJpaEntity(UserDto userDto) {
        if(userDto == null) {
            return null;
        }

        return new UserJpaEntity(
                userDto.id(),
                userDto.username(),
                userDto.email(),
                userDto.password(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.phone(),
                userDto.role()
        );
    }
}
