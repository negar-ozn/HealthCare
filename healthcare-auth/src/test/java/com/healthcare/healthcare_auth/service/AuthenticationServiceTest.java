package com.healthcare.healthcare_auth.service;

import com.healthcare.healthcare_auth.dto.RegisterRequest;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.exception.EmailAlreadyExistsException;
import com.healthcare.healthcare_auth.exception.UsernameAlreadyExistsException;
import com.healthcare.healthcare_auth.kafka.producer.NotificationProducer;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UsersRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationProducer notificationProducer;
    @InjectMocks
    private AuthenticationService authenticationService;


    //Successful Registration
    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "negar@gmail.com",
                "Password@123",
                "ROLE_PATIENT"
        );

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");

        authenticationService.register(request);

        verify(userRepository).save(any(Users.class));
        verify(notificationProducer).publish(any());
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
