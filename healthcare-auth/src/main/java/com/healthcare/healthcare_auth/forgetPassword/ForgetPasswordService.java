package com.healthcare.healthcare_auth.forgetPassword;

import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.NotificationSource;
import com.healthcare.common_kafka.enums.NotificationType;
import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.Status;
import com.healthcare.healthcare_auth.exception.InvalidPasswordResetTokenException;
import com.healthcare.healthcare_auth.exception.EmailNotFoundException;
import com.healthcare.healthcare_auth.exception.PasswordResetTokenExpiredException;
import com.healthcare.healthcare_auth.repository.RefreshTokenRepository;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ForgetPasswordService {

    @Value("${app.base-url}")
    private String resetPasswordEmailURL;

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

    public void forgetPassword(String email) {

        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        if (usersOptional.isEmpty()) {
            throw new EmailNotFoundException("Email not found.");
        }
        Users user = usersOptional.get();
        passwordResetTokenRepository.deleteByUser(user);
        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .createdAt(Instant.now())
                .expiryDate(Instant.now().plus(30, ChronoUnit.MINUTES))
                .build();
        passwordResetTokenRepository.save(token);

        // Publish password reset email notification event
        commonKafkaProducer.publishPasswordResetEmailEvent(NotificationEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .type(NotificationType.PASSWORD_RESET)
                .recipient(user.getEmail())
                .sender(NotificationSource.AUTH)
                .subject("Reset your password.")
                .variables(Map.of("username", user.getUsername(),
                        "resetPasswordLink", resetPasswordLink(token.getToken())))
                .userId(user.getId())
                .eventType(EventType.PASSWORD_RESET)
                .build());
    }

    public String resetPassword(String token) {
        return resetPasswordEmailURL
                + "/api/v1/auth/setNewPassword?token="
                + token;
    }

    public void setNewPassword(String token, ResetPasswordRequest request) {


        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid password rest token."));
        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new PasswordResetTokenExpiredException("Password reset token expired.");
        }

        Users user = resetToken.getUser();
        try {
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            usersRepository.save(user);
            refreshTokenRepository.deleteByUser(user);
            passwordResetTokenRepository.delete(resetToken);

            commonKafkaProducer.publishNewPasswordSubmittedEvent(CoreEvent.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .action(NotificationType.NEW_PASSWORD.name())
                    .entityName(EntityName.USER)
                    .status(Status.SUCCESSFUL.name())
                    .details("New password submitted successfully")
                    .eventType(EventType.NEW_PASSWORD)
                    .build());

        } catch (Exception e) {
            commonKafkaProducer.publishNewPasswordSubmittedEvent(CoreEvent.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .action(NotificationType.NEW_PASSWORD.name())
                    .entityName(EntityName.USER)
                    .status(Status.FAILED.name())
                    .details("Failed to set new password")
                    .eventType(EventType.NEW_PASSWORD)
                    .build());
        }

    }


    private String resetPasswordLink(String resetPasswordToken) {
        return resetPasswordEmailURL
                + "/api/v1/auth/resetPassword?token="
                + resetPasswordToken;

    }
}
