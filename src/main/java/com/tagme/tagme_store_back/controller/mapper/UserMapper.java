package com.tagme.tagme_store_back.controller.mapper;

import com.tagme.tagme_store_back.controller.webModel.request.UserInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.UserUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.model.UserRole;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import static com.tagme.tagme_store_back.web.utils.MimeUtil.getMimeType;

public class UserMapper {
    public static UserResponse fromUserDtoToUserResponse(UserDto userDto) throws IOException {
        if (userDto == null) {
            return null;
        }

        byte[] imageBytes = convertToBytes(userDto.profilePicture());
        String mimeType = getMimeType(userDto.profilePictureName());

        return new UserResponse(
                userDto.id(),
                userDto.username(),
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.phone(),
                imageBytes != null ? "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes) : null,
                userDto.profilePictureName(),
                userDto.role().name()
        );
    }

    public static UserDto fromUserInsertRequestToUserDto(UserInsertRequest request) throws SQLException, IOException {
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
                convertToBlob(request.profilePicture()),
                request.profilePictureName(),
                UserRole.valueOf(request.role())
        );
    }

    public static UserDto fromUserUpdateRequestToUserDto(UserUpdateRequest request) throws SQLException, IOException {
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
                convertToBlob(request.profilePicture()),
                request.profilePictureName(),
                UserRole.valueOf(request.role())
        );
    }

    private static byte[] convertToBytes(Blob blob) {
        if (blob == null) {
            return null;
        }
        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Blob to byte array", e);
        }
    }

    private static Blob convertToBlob(String base64) throws SQLException, IOException {
        if (base64 == null || base64.isBlank()) {
            return null;
        }
        byte[] bytes = Base64.getDecoder().decode(base64);
        return new SerialBlob(bytes);
    }

}
