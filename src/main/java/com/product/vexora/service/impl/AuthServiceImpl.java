package com.product.vexora.service.impl;

import com.product.vexora.dto.LoginDto;
import com.product.vexora.dto.SignupDto;
import com.product.vexora.entity.User;
import com.product.vexora.enums.Role;
import com.product.vexora.exception.InvalidPasswordException;
import com.product.vexora.exception.UserAlreadyExistsException;
import com.product.vexora.exception.UserNotFoundException;
import com.product.vexora.repository.UserRepository;
import com.product.vexora.service.AuthService;
import com.product.vexora.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void signup(SignupDto dto) {

        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new UserAlreadyExistsException(dto.username());
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role() != null ? dto.role() : Role.ROLE_USER);

        userRepository.save(user);
    }

    public String login(LoginDto dto) {

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UserNotFoundException(dto.username()));

        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())) {
            throw new InvalidPasswordException();
        }

        return jwtService.generateToken(user);
    }
}
