package com.healthcare.healthcare_auth.service;

import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.NotificationType;
import com.healthcare.common_kafka.enums.NotificationSource;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.event.*;
import com.healthcare.healthcare_auth.dto.*;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationService;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationToken;
import com.healthcare.healthcare_auth.entity.RefreshToken;
import com.healthcare.healthcare_auth.entity.Role;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.enums.*;
import com.healthcare.healthcare_auth.exception.*;
import com.healthcare.healthcare_auth.repository.RoleRepository;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import com.healthcare.healthcare_auth.security.AuthenticatedUser;
import com.healthcare.healthcare_auth.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class AuthenticationService {

    @Value("${app.base-url}")
    private String emailVerificationURL;

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordSetupService passwordSetupService;
    private final CommonKafkaProducer commonKafkaProducer;

    public AuthenticationService(UsersRepository usersRepository, RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                 JwtService jwtService, RefreshTokenService refreshTokenService,
                                 LoginAttemptService loginAttemptService, EmailVerificationService emailVerificationService,
                                 PasswordSetupService passwordSetupService, CommonKafkaProducer commonKafkaProducer) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.loginAttemptService = loginAttemptService;
        this.emailVerificationService = emailVerificationService;
        this.passwordSetupService = passwordSetupService;
        this.commonKafkaProducer = commonKafkaProducer;
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
        commonKafkaProducer.publishUserRegisteredEvent(UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .verificationLink(verificationLink(emailVerificationToken.getToken()))
                .action(ActionType.REGISTER.name())
                .status(Status.SUCCESSFUL.name())
                .detail("User registered successfully")
                .entityName(EntityName.USER)
                .eventType(EventType.USER_REGISTERED)
                .build());

        // 8. Publish email notification event
        commonKafkaProducer.publishNotificationEvent(NotificationEvent.builder()
                .correlationId(UUID.randomUUID().toString())
                .type(NotificationType.EMAIL_VERIFICATION)
                .recipient(savedUser.getEmail())
                .sender(NotificationSource.AUTH)
                .subject("Verify your email.")
                .variables(Map.of("username", savedUser.getUsername(),
                        "verificationLink", verificationLink(emailVerificationToken.getToken())))
                .eventType(EventType.NOTIFICATION_EVENTS)
                .userId(savedUser.getId())
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

            commonKafkaProducer.publishUserLoginEvent(CoreEvent.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .action(ActionType.LOGIN.name())
                    .entityName(EntityName.USER)
                    .status(Status.SUCCESSFUL.name())
                    .details("User logged in successfully")
                    .eventType(EventType.USER_LOGIN)
                    .build());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getAccessTokenExpiration())
                    .build();

        } catch (BadCredentialsException ex) {
            loginAttemptService.loginFailed(request.username());
            commonKafkaProducer.publishUserLoginEvent(CoreEvent.builder()
                    .userId(SecurityUtils.currentUserId())
                    .username(request.username())
                    .email(SecurityUtils.currentUserEmail())
                    .action(ActionType.LOGIN.name())
                    .entityName(EntityName.USER)
                    .status(Status.FAILED.name())
                    .details("Invalid username or password")
                    .eventType(EventType.USER_LOGIN)
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
        commonKafkaProducer.publishRefreshTokenEvent(CoreEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .action(ActionType.REFRESH_TOKEN.name())
                .entityName(EntityName.USER)
                .status(Status.SUCCESSFUL.name())
                .details("Access token regenerated successfully")
                .eventType(EventType.REFRESH_TOKEN)
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
        commonKafkaProducer.publishUserLogoutEvent(CoreEvent.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .action(ActionType.LOGOUT.name())
                .status(Status.SUCCESSFUL.name())
                .details("User logged out successfully.")
                .eventType(EventType.USER_LOGOUT)
                .entityName(EntityName.USER)
                .build());
    }

    public TokenIntrospectionResponse introspect(TokenIntrospectionRequest request) {

        String token = request.accessToken();
        try {
            String username = jwtService.extractUsername(token);
            Users user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (!jwtService.isTokenValid(token, user)) {
                commonKafkaProducer.publishIntrospectEvent(CoreEvent.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .username(username)
                        .action(ActionType.INTROSPECT.name())
                        .entityName(EntityName.ROLE)
                        .status(Status.FAILED.name())
                        .details("Introspection result: INVALID; username: " + username)
                        .eventType(EventType.INTROSPECT)
                        .build());

                return TokenIntrospectionResponse.builder()
                        .valid(false)
                        .build();
            }

            Set<String> authorities = user.getRoles()
                    .stream()
                    .flatMap(role -> {
                        Stream<String> roleAuthority =
                                Stream.of(role.getRole().name());

                        Stream<String> permissionAuthorities =
                                role.getPermissions()
                                        .stream()
                                        .map(permission -> permission.getPermission().name());

                        return Stream.concat(roleAuthority, permissionAuthorities);
                    })
                    .collect(Collectors.toSet());

            commonKafkaProducer.publishIntrospectEvent(CoreEvent.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(username)
                    .action(ActionType.INTROSPECT.name())
                    .entityName(EntityName.ROLE)
                    .status(Status.SUCCESSFUL.name())
                    .details("Introspection result: VALID; username: " + username)
                    .eventType(EventType.INTROSPECT)
                    .build());

            return TokenIntrospectionResponse.builder()
                    .valid(true)
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .authorities(authorities)
                    .expiresAt(jwtService.extractExpiration(token).toInstant())
                    .build();

        } catch (ExpiredJwtException e) {

            Claims claims = jwtService.extractClaimsIgnoringExpiration(token);
            String username = claims.getSubject();
            Users user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Set<String> authorities = user.getRoles()
                    .stream()
                    .map(role -> role.getRole().name())
                    .collect(Collectors.toSet());

            Instant expiration = e.getClaims().getExpiration().toInstant();

            commonKafkaProducer.publishIntrospectEvent(CoreEvent.builder()
                    .userId(user.getId())
                    .username(username)
                    .email(user.getEmail())
                    .action(ActionType.INTROSPECT.name())
                    .entityName(EntityName.ROLE)
                    .status(Status.FAILED.name())
                    .details("Introspection result: EXPIRED; expired at: " + expiration)
                    .eventType(EventType.INTROSPECT)
                    .build());

            return TokenIntrospectionResponse.builder()
                    .valid(false)
                    .userId(user.getId())
                    .username(username)
                    .email(user.getEmail())
                    .authorities(authorities)
                    .expiresAt(expiration)
                    .build();
        }
    }

    @Transactional
    public InternalUserCreateResponse createInternalUser(InternalUserCreateRequest request) {

        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.role() == null) {
            throw new InvalidRoleException("Role is required");
        }
        if (request.role() == UserRole.ROLE_PATIENT) {
            throw new InvalidRoleException("ROLE_PATIENT must use public registration");
        }
        if (usersRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
        if (usersRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        Role role = roleRepository.findByRole(request.role())
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + request.role()));

        Users user = Users.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .isEnabled(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .build();

        user.addRole(role);
        Users savedUser = usersRepository.save(user);

        passwordSetupService.createPasswordSetupTokenAndSendEmail(savedUser);

        return new InternalUserCreateResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                request.role()
        );
    }

    private String verificationLink(String verificationToken) {
        return emailVerificationURL
                + "/api/v1/auth/verifyEmail?token="
                + verificationToken;
    }
}
