package com.tagme.tagme_store_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagme.tagme_store_back.controller.webModel.request.UserInsertRequest;
import com.tagme.tagme_store_back.controller.webModel.request.UserUpdateRequest;
import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.exception.BusinessException;
import com.tagme.tagme_store_back.domain.exception.ResourceNotFoundException;
import com.tagme.tagme_store_back.domain.model.Page;
import com.tagme.tagme_store_back.domain.model.UserRole;
import com.tagme.tagme_store_back.domain.service.AuthService;
import com.tagme.tagme_store_back.domain.service.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.instancio.Select.field;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // java
    @Nested
    class GetAllTests {
        @DisplayName("Given valid page and size, when getAll is called, then return paged users")
        @Test
        void getAllWithParams() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class).withSeed(30).create();

            var page = new Page<>(
                    List.of(userDto), 1, 1, 1L);

            when(userService.getAll(1, 1)).thenReturn(page);

            mockMvc.perform(get("/users").param("page", "1").param("size", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.data[0].id").value(userDto.id()))
                    .andExpect(jsonPath("$.data[0].username").value(userDto.username()))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

        @DisplayName("Given no params, when getAll is called, then use default page and size")
        @Test
        void getAllDefaultParams() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class).withSeed(31).create();

            var page = new Page<>(
                    List.of(userDto), 1, 10, 1L);

            when(userService.getAll(1, 10)).thenReturn(page);

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10));
        }

        @DisplayName("Given invalid pagination, when service throws BusinessException, then return 400")
        @Test
        void getAllInvalidParams() throws Exception {
            when(userService.getAll(anyInt(), anyInt()))
                    .thenThrow(new BusinessException("Invalid pagination"));

            mockMvc.perform(get("/users").param("page", "0").param("size", "1"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetUserByIdTests {
        @DisplayName("Given existing user ID, when getUserById is called, then return the corresponding UserResponse")
        @Test
        void getUserByIdExistingUser() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class).create();

            when(userService.getById(anyLong())).thenReturn(userDto);

            mockMvc.perform(get("/users/{id}", userDto.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userDto.id()))
                    .andExpect(jsonPath("$.username").value(userDto.username()))
                    .andExpect(jsonPath("$.email").value(userDto.email()));
        }

        @DisplayName("Given non-existing user ID, when getUserById is called, then return 404 Not Found")
        @Test
        void getUserByIdNonExistingUser() throws Exception {
            when(userService.getById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(get("/users/{id}", 9999999L))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateUserTests {
        @DisplayName("Given valid user data, when createUser is called, then return the created UserResponse")
        @Test
        void createUserValidData() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class).withSeed(10).create();

            when(userService.create(any(UserDto.class))).thenReturn(userDto);

            UserInsertRequest userInsertRequest = Instancio.of(UserInsertRequest.class)
                    .set(field(UserInsertRequest.class, "email"), "example@gmail.com")
                    .set(field(UserInsertRequest.class, "role"), UserRole.ADMIN.toString())
                    .set(field(UserInsertRequest.class, "phone"), "+1234567890")
                    .withSeed(10).create();

            System.out.printf("userInsertRequest: %s%n", userInsertRequest);

            String requestBody = objectMapper.writeValueAsString(userInsertRequest);

            mockMvc.perform(post("/users")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(userDto.id()))
                    .andExpect(jsonPath("$.username").value(userDto.username()))
                    .andExpect(jsonPath("$.email").value(userDto.email()));
        }

        @DisplayName("Given invalid user data, when createUser is called, then return 400 Bad Request")
        @Test
        void createUserInvalidData() throws Exception {
            UserInsertRequest userInsertRequest = Instancio.of(UserInsertRequest.class)
                    .set(field(UserInsertRequest.class, "email"), "invalid-email")
                    .set(field(UserInsertRequest.class, "role"), "INVALID_ROLE")
                    .set(field(UserInsertRequest.class, "phone"), "invalid-phone")
                    .withSeed(10).create();

            String requestBody = objectMapper.writeValueAsString(userInsertRequest);
            mockMvc.perform(post("/users")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateUserTests {
        @DisplayName("Given valid user data, when updateUser is called, then return the updated UserResponse")
        @Test
        void updateUserValidData() throws Exception {
            UserDto userDto = Instancio.of(UserDto.class).withSeed(20).create();

            when(userService.update(any(UserDto.class))).thenReturn(userDto);

            UserUpdateRequest userUpdateRequest = Instancio.of(UserUpdateRequest.class)
                    .set(field(UserUpdateRequest.class, "id"), userDto.id())
                    .set(field(UserUpdateRequest.class, "email"), "example@gmail.com")
                    .set(field(UserUpdateRequest.class, "role"), UserRole.ADMIN.toString())
                    .set(field(UserUpdateRequest.class, "phone"), "+1234567890")
                    .withSeed(20).create();

            String requestBody = objectMapper.writeValueAsString(userUpdateRequest);

            mockMvc.perform(put("/users/{id}", userDto.id())
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userDto.id()))
                    .andExpect(jsonPath("$.username").value(userDto.username()))
                    .andExpect(jsonPath("$.email").value(userDto.email()));
        }

        @DisplayName("Given invalid user data, when updateUser is called, then return 400 Bad Request")
        @Test
        void updateUserInvalidData() throws Exception {
            UserUpdateRequest userUpdateRequest = Instancio.of(UserUpdateRequest.class)
                    .set(field(UserUpdateRequest.class, "email"), "invalid-email")
                    .set(field(UserUpdateRequest.class, "role"), "INVALID_ROLE")
                    .set(field(UserUpdateRequest.class, "phone"), "invalid-phone")
                    .withSeed(20).create();

            String requestBody = objectMapper.writeValueAsString(userUpdateRequest);

            mockMvc.perform(put("/users/{id}", userUpdateRequest.id())
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteUserTests {
        @DisplayName("Given existing user ID, when deleteUser is called, then return 204 No Content")
        @Test
        void deleteUserExistingUser() throws Exception {
            doNothing().when(userService).deleteById(anyLong());
            when(authService.getByToken(anyString())).thenReturn(Instancio.of(UserDto.class).set(field(UserDto.class, "id"), 1L).create());

            mockMvc.perform(delete("/users/{id}", 1L)
                        .header("Authorization", "Bearer valid-token")
                    ).andExpect(status().isNoContent());
        }

        @DisplayName("Given non-existing user ID, when deleteUser is called, then return 404 Not Found")
        @Test
        void deleteUserNonExistingUser() throws Exception {
            doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteById(anyLong());
            when(authService.getByToken(anyString())).thenReturn(Instancio.of(UserDto.class).set(field(UserDto.class, "id"), 9999999L).create());

            mockMvc.perform(delete("/users/{id}", 9999999L)
                            .header("Authorization", "Bearer valid-token")
                    ).andExpect(status().isNotFound());
        }
    }
}
