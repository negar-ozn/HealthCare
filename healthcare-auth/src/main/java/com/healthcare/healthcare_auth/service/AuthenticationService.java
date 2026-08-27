package com.healthcare.healthcare_auth.service;

import com.healthcare.healthcare_auth.dto.*;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationService;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationToken;
import com.healthcare.healthcare_auth.entity.RefreshToken;
import com.healthcare.healthcare_auth.entity.Role;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.*;
import com.healthcare.healthcare_auth.feignClient.NotificationClient;
import com.healthcare.healthcare_auth.kafka.event.*;
import com.healthcare.healthcare_auth.exception.*;
import com.healthcare.healthcare_auth.kafka.producer.*;
import com.healthcare.healthcare_auth.repository.RoleRepository;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import com.healthcare.healthcare_auth.security.AuthenticatedUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Transactional
@Service
public class AuthenticationService {

    @Value("${app.verification.base-url}")
    private String emailVerificationURL;

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserLoginProducer userLoginProducer;
    private final UserLogoutProducer userLogoutProducer;
    private final NotificationProducer notificationProducer;
    private final UserRegisteredProducer userRegisteredProducer;
    private final RefreshTokenProducer refreshTokenProducer;
    private final LoginAttemptService loginAttemptService;
    private final EmailVerificationService emailVerificationService;
    private final NotificationClient notificationClient;

    public AuthenticationService(UsersRepository usersRepository, RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                 JwtService jwtService, RefreshTokenService refreshTokenService,
                                 UserLoginProducer userLoginProducer, UserLogoutProducer userLogoutProducer,
                                 NotificationProducer notificationProducer, UserRegisteredProducer userRegisteredProducer,
                                 RefreshTokenProducer refreshTokenProducer, LoginAttemptService loginAttemptService,
                                 EmailVerificationService emailVerificationService, NotificationClient notificationClient) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userLoginProducer = userLoginProducer;
        this.userLogoutProducer = userLogoutProducer;
        this.notificationProducer = notificationProducer;
        this.userRegisteredProducer = userRegisteredProducer;
        this.refreshTokenProducer = refreshTokenProducer;
        this.loginAttemptService = loginAttemptService;
        this.emailVerificationService = emailVerificationService;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        // 1. Validate role FIRST (before any DB or expensive operations)
        if (request.role() == null || !UserRole.ROLE_PATIENT.name().equals(request.role())) {
            throw new InvalidRoleException("Only ROLE_PATIENT is allowed during self-registration");
        }

        // 2. Basic defensive checks (even if @Valid exists on controller)
        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // 3. Uniqueness checks
        if (usersRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
        if (usersRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        // 4. Load role
        Role patientRole = roleRepository.findByRole(UserRole.ROLE_PATIENT)
                .orElseThrow(() -> new RoleNotFoundException("Default role ROLE_PATIENT not found"));

        String encryptedPassword = passwordEncoder.encode(request.password());

        // 5. Create and save user
        Users user = Users.builder()
                .username(request.username())
                .password(encryptedPassword)
                .email(request.email())
                .isEnabled(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .createdAt(Instant.now())
                .build();
        user.addRole(patientRole);
        Users savedUser = usersRepository.save(user);

        // 6. Create email verification token
        EmailVerificationToken emailVerificationToken = emailVerificationService.createToken(savedUser);

        // 7. Publish UserRegistered event
        userRegisteredProducer.publishUserRegisteredEvent(UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .verificationLink(verificationLink(String.valueOf(emailVerificationToken)))
                .auction(ActionType.REGISTER.name())
                .status(Status.SUCCESSFUL.name())
                .detail("User registered successfully")
                .entityName(EntityName.USER.name())
                .build());

        // 8. Publish notification event
        notificationProducer.publish(NotificationEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .type(NotificationType.EMAIL_VERIFICATION)
                .recipient(savedUser.getEmail())
                .sender(NotificationSource.AUTH)
                .subject("Verify your email.")
                .variables(Map.of("username", user.getUsername(),
                        "verificationLink", verificationLink(String.valueOf(emailVerificationToken))))
                .build());

        // 9. Calls sendVerificationEmail method in healthcare_notification to send email
        notificationClient.sendVerificationEmail(VerificationEmailRequest.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .verificationLink(verificationLink(String.valueOf(emailVerificationToken)))
                .build());

        return RegisterResponse.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .message("Registration successful. Please check your email to verify your account.")
                .emailVerificationRequired(true)
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        Users user;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            AuthenticatedUser userPrincipal = (AuthenticatedUser) authentication.getPrincipal();

            if (userPrincipal == null) {
                throw new UserNotFoundException("User not found");
            }
            user = userPrincipal.getUser();

            if (!user.isEnabled()) {
                throw new DisabledException("Account is disabled. Please verify your email.");
            }
            if (user.isAccountLocked()) {
                throw new LockedException("Account is locked due to too many failed attempts.");
            }

            loginAttemptService.loginSucceed(user);
            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            userLoginProducer.publishUserLoginEvent(UserLoginEvent.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .action(ActionType.LOGIN.name())
                    .entityName(EntityName.USER.name())
                    .status(Status.SUCCESSFUL.name())
                    .detail("User logged in successfully")
                    .build());
            ;

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getAccessTokenExpiration())
                    .build();

        } catch (BadCredentialsException ex) {
            loginAttemptService.loginFailed(request.username());
            userLoginProducer.publishUserLoginEvent(UserLoginEvent.builder()
                    .userId(request.userId())
                    .username(request.username())
                    .action(ActionType.LOGIN.name())
                    .entityName(EntityName.USER.name())
                    .status(Status.FAILED.name())
                    .detail("Invalid username or password")
                    .build());
            throw new BadCredentialsException("Invalid username or password");
        } catch (DisabledException ex) {
            throw new DisabledException("Account is disabled. Please verify your email");
        } catch (LockedException ex) {
            throw new LockedException("Account is locked due to too many failed attempts");
        }
    }

    public LoginResponse refreshAccessToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        Users user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        refreshTokenProducer.publishRefreshTokenEvent(RefreshTokenEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .action(ActionType.REFRESH_TOKEN.name())
                .entityName(EntityName.USER.name())
                .status(Status.INVALID_REFRESH_TOKEN.name())
                .detail("Refresh token is invalid.")
                .build());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiration())
                .build();
    }

    public void logout(LogoutRequest request) {

        RefreshToken refreshToken = refreshTokenService.findByToken(request.refreshToken());
        refreshTokenService.revoke(refreshToken);
        Users user = refreshToken.getUser();
        userLogoutProducer.publishUserLogoutEvent(UserLogoutEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .action(ActionType.LOGOUT.name())
                .status(Status.SUCCESSFUL.name())
                .detail("User logged out successfully.")
                .build());
    }

    public TokenIntrospectionResponse introspect(TokenIntrospectionRequest request) {

        String token = request.accessToken();
        String username = jwtService.extractUsername(token);
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!jwtService.isTokenValid(token, user)) {
            return TokenIntrospectionResponse.builder().valid(false).build();
        }
        return TokenIntrospectionResponse.builder()
                .valid(true)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(Collections.singleton(user.getRoles().toString()))
                .expiresAt(jwtService.extractExpiration(token).toInstant())
                .build();
    }

    private String verificationLink(String verificationToken) {
        return emailVerificationURL
                + "/api/v1/auth/verifyEmail?token="
                + verificationToken;
    }
}
