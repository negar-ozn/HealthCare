package com.healthcare.healthcare_auth.config;

import com.healthcare.healthcare_auth.entity.Role;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.UserRole;
import com.healthcare.healthcare_auth.repository.RoleRepository;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Value("${app.initial-admin.username}")
    private String username;

    @Value("${app.initial-admin.email}")
    private String email;

    @Value("${app.initial-admin.password}")
    private String password;

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UsersRepository usersRepository, RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (usersRepository.existsByUsername(username)) {
            return;
        }

        Role adminRole = roleRepository.findByRole(UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found"));

        Users admin = Users.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .isEnabled(true)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .build();

        admin.addRole(adminRole);
        usersRepository.save(admin);
    }
}
