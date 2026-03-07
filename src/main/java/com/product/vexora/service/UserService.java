package com.product.vexora.service;

import com.product.vexora.dto.request.CreateUserRequest;
import com.product.vexora.dto.response.UserResponse;
import com.product.vexora.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User loadByUsername(String username);

    List<UserResponse> getAllUsers(String currentUserUsername);

    UserResponse createUser(CreateUserRequest request, String currentUserUsername);

    UserResponse getUserById(UUID id);

    void deleteUser(UUID id, String currentUserUsername);

    UserResponse updateUser(UUID id, CreateUserRequest request, String currentUserUsername);
}
