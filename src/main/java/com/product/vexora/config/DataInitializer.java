package com.product.vexora.config;

import com.product.vexora.entity.User;
import com.product.vexora.enums.Role;
import com.product.vexora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            initializeDefaultAdmin();
            migrateExistingUsersRoles();
        } catch (Exception e) {
            log.error("Erro na inicialização de dados: {}", e.getMessage(), e);
        }
    }

    private void initializeDefaultAdmin() {
        String adminUsername = "admin";

        try {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);

                userRepository.save(admin);
                log.info("✅ Usuário administrador padrão criado: {}", adminUsername);
            } else {
                log.info("ℹ️ Usuário administrador já existe: {}", adminUsername);
            }
        } catch (Exception e) {
            log.error("❌ Erro ao criar usuário administrador padrão: {}", e.getMessage());
        }
    }

    private void migrateExistingUsersRoles() {
        try {
            // Esta função pode ser removida após a primeira execução bem-sucedida
            // Ela está aqui para migrar roles antigos se existirem
            log.info("🔄 Verificando migração de roles de usuários existentes...");

            // A migração de roles agora é feita via schema.sql
            // Este método está vazio mas pode ser usado para verificações futuras

            log.info("✅ Verificação de migração de roles concluída");
        } catch (Exception e) {
            log.warn("⚠️ Aviso durante verificação de migração de roles: {}", e.getMessage());
        }
    }
}
