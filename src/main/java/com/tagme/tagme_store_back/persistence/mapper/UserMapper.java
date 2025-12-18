package com.tagme.tagme_store_back.persistence.mapper;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.User;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.Objects;

public class UserMapper {
    public static UserDto fromUserJpaEntityToUserDto(UserJpaEntity userJpaEntity) {
        if(userJpaEntity == null) {
            return null;
        }

        Blob blobImage = null;

        try {
            byte[] imageBytes = userJpaEntity.getProfilePicture();
            if (imageBytes != null) {
                blobImage = new SerialBlob(imageBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo byte[] a Blob", e);
        }

        return new UserDto(
                userJpaEntity.getId(),
                userJpaEntity.getUsername(),
                userJpaEntity.getPassword(),
                userJpaEntity.getEmail(),
                userJpaEntity.getFirstName(),
                userJpaEntity.getLastName(),
                userJpaEntity.getPhone(),
                blobImage,
                userJpaEntity.getProfilePictureName(),
                userJpaEntity.getRole()
        );
    }

    public static UserJpaEntity fromUserDtoToUserJpaEntity(UserDto userDto) {
        if(userDto == null) {
            return null;
        }

        byte[] imageBytes = null;

        try {
            Blob blob = userDto.profilePicture();
            if (blob != null) {
                imageBytes = blob.getBytes(1, (int) blob.length());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo Blob a byte[]", e);
        }

        return new UserJpaEntity(
                userDto.id(),
                userDto.username(),
                userDto.email(),
                userDto.password(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.phone(),
                imageBytes,
                userDto.profilePictureName(),
                userDto.role()
        );
    }
}
