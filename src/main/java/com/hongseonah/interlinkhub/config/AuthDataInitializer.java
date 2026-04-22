package com.hongseonah.interlinkhub.config;

import com.hongseonah.interlinkhub.domain.auth.entity.AppUser;
import com.hongseonah.interlinkhub.domain.auth.entity.UserRole;
import com.hongseonah.interlinkhub.domain.auth.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthDataInitializer {

    @Bean
    CommandLineRunner initUsers(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            repository.save(createUser("admin", "admin123", "관리자", UserRole.ADMIN, passwordEncoder));
            repository.save(createUser("operator", "operator123", "운영자", UserRole.OPERATOR, passwordEncoder));
            repository.save(createUser("viewer", "viewer123", "조회자", UserRole.VIEWER, passwordEncoder));
        };
    }

    private AppUser createUser(String username, String password, String displayName, UserRole role, PasswordEncoder encoder) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setDisplayName(displayName);
        user.setRole(role);
        user.setEnabled(true);
        return user;
    }
}