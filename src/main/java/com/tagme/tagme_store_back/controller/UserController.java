package com.tagme.tagme_store_back.controller;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.tagme.tagme_store_back.controller.mapper.CategoryMapper;
import com.tagme.tagme_store_back.controller.mapper.UserMapper;
import com.tagme.tagme_store_back.controller.webModel.request.PasswordRequest;
import com.tagme.tagme_store_back.controller.webModel.request.UserInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.UserUpdateRequest;
import com.tagme.tagme_store_back.controller.webModel.response.CategoryResponse;
import com.tagme.tagme_store_back.controller.webModel.response.UserResponse;
import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.InvalidCredentialsException;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.UserService;
import com.tagme.tagme_store_back.domain.validation.DtoValidator;
import com.tagme.tagme_store_back.web.context.AuthContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    private UserService userService;
    private AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllCategories(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        Page<UserDto> userDtos = userService.getAll(page, size);

        List<UserResponse> userResponseList = userDtos.data().stream()
                .map(dto -> {
                    try {
                        return UserMapper.fromUserDtoToUserResponse(dto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        Page<UserResponse> userResponsePage = new Page<>(userResponseList, page, size, userDtos.totalElements(), userDtos.totalPages());

        return new ResponseEntity<>(userResponsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws IOException{
        UserResponse userResponse = UserMapper.fromUserDtoToUserResponse(userService.getById(id));
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserInsertRequest userInsertRequest) throws SQLException, IOException {
        UserDto userDto = UserMapper.fromUserInsertRequestToUserDto(userInsertRequest);
        DtoValidator.validate(userDto);

        UserResponse createdUser = UserMapper.fromUserDtoToUserResponse(userService.create(userDto));
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/customer")
    public ResponseEntity<UserResponse> createUserCustomer(@RequestBody UserInsertRequest userInsertRequest) throws SQLException, IOException {
        UserDto userDto = UserMapper.fromUserInsertRequestToUserDtoCustomer(userInsertRequest);
        DtoValidator.validate(userDto);

        UserResponse createdUser = UserMapper.fromUserDtoToUserResponse(userService.create(userDto));
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) throws SQLException, IOException {
        if(!id.equals(userUpdateRequest.id())) {
            throw new BusinessException("ID in path and request body do not match");
        }

        UserDto userDto = UserMapper.fromUserUpdateRequestToUserDto(userUpdateRequest);
        DtoValidator.validate(userDto);

        UserResponse updatedUser = UserMapper.fromUserDtoToUserResponse(userService.update(userDto));
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<UserResponse> updateUserPassword(@PathVariable Long id, @RequestBody PasswordRequest passwordRequest) throws SQLException, IOException {
        DtoValidator.validate(passwordRequest);

        if (authService.isCurrentPassword(id, passwordRequest.currentPassword()) == false) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        if (!passwordRequest.newPassword().equals(passwordRequest.newPasswordConfirmation())) {
            throw new BusinessException("New password and confirmation must match");
        }

        userService.updatePassword(id, passwordRequest.newPassword());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        UserResponse sessionUser = AuthContext.getUser();
        if (sessionUser == null) {
            throw new InvalidCredentialsException("Authentication required to delete user");
        }

        if(!sessionUser.id().equals(id) && !sessionUser.role().toString().equals("ADMIN")) {
            throw new BusinessException("Users can only delete their own accounts");
        }

        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
