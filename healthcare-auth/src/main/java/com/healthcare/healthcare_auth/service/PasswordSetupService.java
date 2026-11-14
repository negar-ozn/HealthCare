package com.healthcare.healthcare_auth.service;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.enums.NotificationSource;
import com.healthcare.common_kafka.enums.NotificationType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_auth.dto.PasswordSetupRequest;
import com.healthcare.healthcare_auth.entity.PasswordSetupToken;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.ActionType;
import com.healthcare.healthcare_auth.enums.Status;
import com.healthcare.healthcare_auth.exception.InvalidPasswordSetupTokenException;
import com.healthcare.healthcare_auth.exception.PasswordSetupTokenAlreadyActivatedException;
import com.healthcare.healthcare_auth.exception.PasswordSetupTokenAlreadyUsedException;
import com.healthcare.healthcare_auth.exception.PasswordSetupTokenExpiredException;
import com.healthcare.healthcare_auth.repository.PasswordSetupTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordSetupService {

    private final PasswordSetupTokenRepository tokenRepository;
    private final CommonKafkaProducer commonKafkaProducer;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Value("${app.password-setup.token-expiration-minutes:30}")
    private long expirationMinutes;

    @Value("${app.frontend.password-setup-url}")
    private String passwordSetupUrl;

    public PasswordSetupService(PasswordSetupTokenRepository tokenRepository, CommonKafkaProducer commonKafkaProducer,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.commonKafkaProducer = commonKafkaProducer;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createPasswordSetupTokenAndSendEmail(Users user) {

        // 1. Generate a cryptographically secure random token.
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // 2. Hash the token before storing it.
        String tokenHash = hashToken(rawToken);

        // 3. Invalidate any existing unused setup tokens for this user.
        tokenRepository.invalidateUnusedTokensByUserId(user.getId());

        // 4. Create and save the token.
        PasswordSetupToken setupToken = PasswordSetupToken.builder()
                .tokenHash(tokenHash)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(expirationMinutes * 60))
                .used(false)
                .build();

        tokenRepository.save(setupToken);

        // 5. Build the setup link.
        String setupLink = passwordSetupUrl + "?token=" + rawToken;

        // 6. Publish the link email sent event.
        commonKafkaProducer.publishPersonnelPasswordSetupEmailNotificationEvent(NotificationEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .type(NotificationType.PERSONNEL_PASSWORD_SETUP_LINK)
                .recipient(user.getEmail())
                .sender(NotificationSource.AUTH)
                .subject("Set up your healthcare account password")
                .variables(Map.of(
                        "username", user.getUsername(),
                        "passwordSetupLink", setupLink
                ))
                .eventType(EventType.PERSONNEL_PASSWORD_SETUP_LINK)
                .userId(user.getId())
                .build()
        );
    }

    @Transactional
    public void setupPassword(PasswordSetupRequest request) {

        // 1. Hash the raw token received from the HTML page.
        String tokenHash = hashToken(request.token());

        // 2. Find and lock the token.
        PasswordSetupToken setupToken = tokenRepository.findByTokenHashForUpdate(tokenHash)
                .orElseThrow(() -> new InvalidPasswordSetupTokenException("Invalid password setup token")); //400

        // 3. Reject a token that has already been used.
        if (setupToken.isUsed())
            throw new PasswordSetupTokenAlreadyUsedException("This password setup link has already been used"); //410

        // 4. Reject an expired token.
        if (!setupToken.getExpiryDate().isAfter(Instant.now()))
            throw new PasswordSetupTokenExpiredException("This password setup link has expired"); //410

        // 5. Get the user associated with the token.
        Users user = setupToken.getUser();

        // 6. Only allow initial setup for an inactive account.
        if (user.isEnabled())
            throw new PasswordSetupTokenAlreadyActivatedException("This account has already been activated"); //410

        // 7. Encode the password. Never store the raw password.
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // 8. Activate the account.
        user.setEnabled(true);

        // 9. Mark the token as used.
        setupToken.setUsed(true);

        // 10. Publish the personnel password setup event.
        commonKafkaProducer.publishPersonnelPasswordSetupNotificationEvent(CoreEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .action(ActionType.PERSONNEL_PASSWORD_SETUP.name())
                .entityName(EntityName.USER)
                .status(Status.SUCCESSFUL.name())
                .details("Personnel " + user.getUsername() + " set a password successfully.")
                .eventType(EventType.PERSONNEL_PASSWORD_SETUP)
                .build());
    }


    private String hashToken(String token) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", exception);
        }
    }
}
