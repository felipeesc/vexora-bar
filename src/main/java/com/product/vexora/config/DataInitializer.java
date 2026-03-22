package com.product.vexora.config;

import com.product.vexora.entity.User;
import com.product.vexora.enums.Role;
import com.product.vexora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";

    @Override
    public void run(String... args) throws Exception {
        try {
            initializeDefaultAdmin();
        } catch (Exception e) {
            log.error("Erro na inicialização de dados: {}", e.getMessage(), e);
        }
    }

    private void initializeDefaultAdmin() {
        try {
            if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
                // Gerar senha aleatória segura
                String generatedPassword = generateSecurePassword();

                User admin = new User();
                admin.setUsername(ADMIN_USERNAME);
                admin.setPasswordHash(passwordEncoder.encode(generatedPassword));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);

                userRepository.save(admin);
                log.info("═══════════════════════════════════════════════════════════════");
                log.info("✅ Usuário administrador padrão criado!");
                log.info("📧 Username: {}", ADMIN_USERNAME);
                log.info("🔑 Senha: {}", generatedPassword);
                log.info("⚠️  IMPORTANTE: Altere esta senha após o primeiro login!");
                log.info("═══════════════════════════════════════════════════════════════");
            } else {
                log.info("ℹ️ Usuário administrador já existe: {}", ADMIN_USERNAME);
            }
        } catch (Exception e) {
            log.error("❌ Erro ao criar usuário administrador padrão: {}", e.getMessage());
        }
    }

    /**
     * Gera uma senha segura com letras maiúsculas, minúsculas, números e caracteres especiais
     */
    private String generateSecurePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String special = "@$!%*?&#";
        String allChars = upperCase + lowerCase + numbers + special;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Garantir pelo menos um de cada tipo
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        // Completar com caracteres aleatórios até 16 caracteres
        for (int i = 4; i < 16; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Embaralhar a senha
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }
}
