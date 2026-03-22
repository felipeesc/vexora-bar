package com.product.vexora.controller;

import com.product.vexora.dto.LoginDto;
import com.product.vexora.dto.SignupDto;
import com.product.vexora.entity.User;
import com.product.vexora.enums.Role;
import com.product.vexora.repository.UserRepository;
import com.product.vexora.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto dto) {
        authService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        String token = authService.login(dto);
        User user = userRepository.findByUsername(dto.username()).orElseThrow();
        return ResponseEntity.ok(Map.of(
            "token", token,
            "role", user.getRole().name(),
            "username", user.getUsername()
        ));
    }

    /**
     * Endpoint de setup: promove o usuário informado para ADMIN.
     * Só funciona se ainda não houver nenhum ADMIN no sistema.
     * Após criar o primeiro ADMIN, este endpoint retorna 403.
     */
    @PostMapping("/setup-admin")
    public ResponseEntity<?> setupAdmin(@RequestBody Map<String, String> body) {
        boolean hasAdmin = userRepository.existsByRole(Role.ADMIN);
        if (hasAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Setup já foi realizado. Já existe um ADMIN cadastrado."));
        }

        String username = body.get("username");
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "username é obrigatório"));
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Usuário não encontrado: " + username));
        }

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "message", "Usuário " + username + " promovido para ADMIN com sucesso.",
            "username", username,
            "role", "ADMIN"
        ));
    }
}
