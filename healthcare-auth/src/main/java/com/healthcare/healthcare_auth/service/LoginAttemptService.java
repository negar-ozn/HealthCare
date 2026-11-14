package com.healthcare.healthcare_auth.service;

import com.healthcare.healthcare_auth.configProperty.AccountSecurityProperties;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class LoginAttemptService {
    
    private final AccountSecurityProperties accountSecurityProperties;
    private final UsersRepository usersRepository;

    public LoginAttemptService(AccountSecurityProperties accountSecurityProperties, UsersRepository usersRepository) {
        this.accountSecurityProperties = accountSecurityProperties;
        this.usersRepository = usersRepository;
    }

    public void loginSucceed(Users user) {

        user.setFailedLoginAttempts(0);
        usersRepository.save(user);
    }

    public void loginFailed(String username) {

        usersRepository.findByUsername(username)
                .ifPresent(user -> {
                    int attempts = user.getFailedLoginAttempts() + 1;
                    user.setFailedLoginAttempts(attempts);

                    if (attempts >= accountSecurityProperties.getMaxFailedAttempts()) {
                        user.setAccountLocked(true);
                        user.setLockTime(Instant.now());
                    }
                    usersRepository.save(user);

                });
    }

    public void unlockIfExpired(Users user) {

        if (!user.isAccountLocked()) {
            return;
        }
        if (user.getLockTime() == null) {
            return;
        }
        Instant unlockTime = user.getLockTime().plus(accountSecurityProperties.getLockDuration(), ChronoUnit.MINUTES);
        if (Instant.now().isAfter(unlockTime)) {
            user.setAccountLocked(false);
            user.setFailedLoginAttempts(0);
            user.setLockTime(null);

            usersRepository.save(user);
        }
    }
}
