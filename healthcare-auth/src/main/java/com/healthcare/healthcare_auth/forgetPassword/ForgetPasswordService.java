package com.healthcare.healthcare_auth.forgetPassword;

import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.*;
import com.healthcare.healthcare_auth.exception.InvalidPasswordResetTokenException;
import com.healthcare.healthcare_auth.exception.PasswordResetTokenExpiredException;
import com.healthcare.healthcare_auth.repository.RefreshTokenRepository;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional
public class ForgetPasswordService {

    private final UsersRepository usersRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CommonKafkaProducer commonKafkaProducer;

    public ForgetPasswordService(UsersRepository usersRepository, PasswordResetTokenRepository passwordResetTokenRepository,
                                 PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, CommonKafkaProducer commonKafkaProducer) {
        this.usersRepository = usersRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    public void forgotPassword(String email) {

        usersRepository.findByEmail(email)
                .ifPresent(user -> {
                    passwordResetTokenRepository.deleteByUser(user);
                    PasswordResetToken token =
                            PasswordResetToken.builder()
                                    .token(UUID.randomUUID().toString())
                                    .user(user)
                                    .createdAt(Instant.now())
                                    .expiryDate(Instant.now().plus(30, ChronoUnit.MINUTES))
                                    .build();

                    passwordResetTokenRepository.save(token);

                    commonKafkaProducer.publishPasswordResetEvent(CoreEvent.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .action(ActionType.RESET_PASSWORD.name())
                            .entityName(com.healthcare.common_kafka.enums.EntityName.valueOf(EntityName.PASSWORD_RESET_TOKEN.name()))
                            .status(Status.SUCCESSFUL.name())
                            .details("Password reset successfully.")
                            .build());

                });
    }

    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid password rest token."));
        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new PasswordResetTokenExpiredException("Password reset token expired.");
        }

        Users user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        usersRepository.save(user);
        refreshTokenRepository.deleteByUser(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
