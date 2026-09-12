package com.healthcare.healthcare_auth.emailVerification;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.common_kafka.enums.NotificationSource;
import com.healthcare.common_kafka.enums.NotificationType;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.ActionType;
import com.healthcare.healthcare_auth.enums.Status;
import com.healthcare.healthcare_auth.exception.InvalidEmailVerificationTokenException;
import com.healthcare.healthcare_auth.exception.EmailVerificationTokenExpiredException;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UsersRepository userRepository;
    private final CommonKafkaProducer commonKafkaProducer;

    public EmailVerificationService(EmailVerificationTokenRepository emailVerificationTokenRepository,
                                    UsersRepository userRepository, CommonKafkaProducer commonKafkaProducer) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.userRepository = userRepository;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    public EmailVerificationToken createToken(Users user) {
        emailVerificationTokenRepository.deleteByUser(user);
        EmailVerificationToken token =
                EmailVerificationToken.builder()
                        .token(UUID.randomUUID().toString())
                        .user(user)
                        .createdAt(Instant.now())
                        .expiryDate(Instant.now().plus(24, ChronoUnit.HOURS))
                        .build();
        return emailVerificationTokenRepository.save(token);
    }

    @Transactional
    public void verify(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                        .orElseThrow(() -> new InvalidEmailVerificationTokenException("Invalid verification token."));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            emailVerificationTokenRepository.delete(verificationToken);
            throw new EmailVerificationTokenExpiredException("Verification token expired.");
        }

        Users user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        emailVerificationTokenRepository.delete(verificationToken);

        // Send Email Verified event
        commonKafkaProducer.publishEmailVerifiedEvent(CoreEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .action(ActionType.EMAIL_VERIFIED.name())
                .entityName(EntityName.USER)
                .status(Status.EMAIL_VERIFIED.name())
                .details("Email verified successfully")
                .eventType(EventType.EMAIL_VERIFIED)
                .build());

        // Send welcome email notification
        commonKafkaProducer.publishWelcomeEvent(NotificationEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .type(NotificationType.WELCOME)
                .recipient(user.getEmail())
                .sender(NotificationSource.AUTH)
                .subject("Welcome to Healthcare.")
                .variables(Map.of("username", user.getUsername()))
                .eventType(EventType.WELCOME_EMAIL)
                .userId(user.getId())
                .build());
    }
}
