package com.product.vexora.service.impl;

import com.product.vexora.dto.request.CreateUserRequest;
import com.product.vexora.dto.response.UserResponse;
import com.product.vexora.entity.User;
import com.product.vexora.enums.Role;
import com.product.vexora.exception.UnauthorizedRoleException;
import com.product.vexora.exception.UserNotFoundException;
import com.product.vexora.repository.UserRepository;
import com.product.vexora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User loadByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<UserResponse> getAllUsers(String currentUserUsername) {
        User currentUser = loadByUsername(currentUserUsername);

        // Apenas ADMIN pode listar todos os usuários
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedRoleException("Apenas administradores podem listar usuários");
        }

        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request, String currentUserUsername) {
        User currentUser = loadByUsername(currentUserUsername);

        // Validar permissões de criação
        validateUserCreationPermissions(currentUser, request.role());

        // Verificar se o usuário já existe
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Usuário com esse username já existe");
        }

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPasswordHash(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        newUser.setEnabled(true);

        User savedUser = userRepository.save(newUser);
        return toUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return toUserResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id, String currentUserUsername) {
        User currentUser = loadByUsername(currentUserUsername);

        // Apenas ADMIN pode deletar usuários
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedRoleException("Apenas administradores podem deletar usuários");
        }

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        // Não pode deletar a si mesmo
        if (currentUser.getId().equals(userToDelete.getId())) {
            throw new UnauthorizedRoleException("Não é possível deletar seu próprio usuário");
        }

        userRepository.delete(userToDelete);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, CreateUserRequest request, String currentUserUsername) {
        User currentUser = loadByUsername(currentUserUsername);

        // Validar permissões de atualização
        validateUserCreationPermissions(currentUser, request.role());

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        userToUpdate.setUsername(request.username());
        if (request.password() != null && !request.password().isEmpty()) {
            userToUpdate.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        userToUpdate.setRole(request.role());

        User updatedUser = userRepository.save(userToUpdate);
        return toUserResponse(updatedUser);
    }

    private void validateUserCreationPermissions(User currentUser, Role targetRole) {
        switch (currentUser.getRole()) {
            case ADMIN:
                // ADMIN pode criar qualquer role
                break;
            case GERENTE:
                // GERENTE pode criar apenas FUNCIONARIO
                if (targetRole != Role.FUNCIONARIO) {
                    throw new UnauthorizedRoleException("Gerente pode criar apenas usuários do tipo FUNCIONARIO");
                }
                break;
            case FUNCIONARIO:
                // FUNCIONARIO não pode criar usuários
                throw new UnauthorizedRoleException("Funcionário não tem permissão para criar usuários");
            default:
                throw new UnauthorizedRoleException("Role não autorizado");
        }
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.isEnabled()
        );
    }
}
