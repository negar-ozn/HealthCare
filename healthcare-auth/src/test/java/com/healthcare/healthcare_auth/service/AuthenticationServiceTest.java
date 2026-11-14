package com.healthcare.healthcare_auth.service;

import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_auth.dto.RegisterRequest;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationToken;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationService;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.entity.Role;
import com.healthcare.healthcare_auth.enums.UserRole;
import com.healthcare.healthcare_auth.exception.EmailAlreadyExistsException;
import com.healthcare.healthcare_auth.exception.UsernameAlreadyExistsException;
import com.healthcare.healthcare_auth.repository.RoleRepository;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UsersRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private CommonKafkaProducer commonKafkaProducer;


    //Successful Registration
    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "negar",
                "negar@gmail.com",
                "Password@123",
                "ROLE_PATIENT"
        );

        Role patientRole = Role.builder()
                .role(UserRole.ROLE_PATIENT)
                .build();

        when(roleRepository.findByRole(UserRole.ROLE_PATIENT))
                .thenReturn(Optional.of(patientRole));

        EmailVerificationToken verificationToken =
                mock(EmailVerificationToken.class);

        when(userRepository.existsByUsername(request.username()))
                .thenReturn(false);

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        when(userRepository.save(any(Users.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(emailVerificationService.createToken(any(Users.class)))
                .thenReturn(verificationToken);

        when(verificationToken.getToken())
                .thenReturn("test-verification-token");

        authenticationService.register(request);

        verify(userRepository).save(any(Users.class));
        verify(roleRepository).findByRole(UserRole.ROLE_PATIENT);
        verify(emailVerificationService).createToken(any(Users.class));
        verify(commonKafkaProducer).publishUserRegisteredEvent(any());
        verify(commonKafkaProducer).publishNotificationEvent(any());
    }


    //Duplicate Email
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "negar@gmail.com",
                "Password@123",
                "ROLE_PATIENT"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authenticationService.register(request));
        verify(userRepository, never()).save(any());
    }


    //Duplicate Username
    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "negar@gmail.com",
                "Password@123",
                "ROLE_PATIENT"
        );

        when(userRepository.existsByUsername(request.username())).thenReturn(true);
        assertThrows(UsernameAlreadyExistsException.class, () -> authenticationService.register(request)
        );
    }

    //Login
    // ✓ Login Success
    // ✓ Wrong Password
    // ✓ User Disabled
    // ✓ Email Not Verified
    // ✓ Account Locked
    // ✓ Invalid Credentials
    //Refresh
    // ✓ Refresh Success
    // ✓ Refresh Expired
    // ✓ Refresh Revoked
    // ✓ Invalid Refresh
    //Logout
    // ✓ Logout Success
    // ✓ Logout Twice
    // ✓ Invalid Refresh
    //Email Verification
    // ✓ Valid Token
    // ✓ Invalid Token
    // ✓ Expired Token
    // ✓ Already Verified
    //Forgot Password
    // ✓ Existing Email
    // ✓ Unknown Email
    // ✓ Expired Token
    //Reset Password
    // ✓ Success
    // ✓ Invalid Token
    // ✓ Expired Token
    // ✓ Weak Password

}
