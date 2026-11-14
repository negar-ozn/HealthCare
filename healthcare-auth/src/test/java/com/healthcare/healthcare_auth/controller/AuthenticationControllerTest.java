package com.healthcare.healthcare_auth.controller;

import com.healthcare.healthcare_auth.config.SecurityConfig;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationService;
import com.healthcare.healthcare_auth.dto.RegisterRequest;
import com.healthcare.healthcare_auth.forgetPassword.ForgetPasswordService;
import com.healthcare.healthcare_auth.security.CustomUserDetailsService;
import com.healthcare.healthcare_auth.security.JwtAccessDeniedHandler;
import com.healthcare.healthcare_auth.security.JwtAuthenticationEntryPoint;
import com.healthcare.healthcare_auth.service.AuthenticationService;
import com.healthcare.healthcare_auth.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthenticationController.class,
        properties = "app.internal.service-key=test-internal-key")
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    AuthenticationService authenticationService;
    @MockitoBean
    private EmailVerificationService emailVerificationService;
    @MockitoBean
    private ForgetPasswordService forgetPasswordService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockitoBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    void shouldRegisterSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "negar@gmail.com",
                "Password@123",
                "ROLE_PATIENT"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(authenticationService).register(any(RegisterRequest.class));

    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "Password@123",
                "wrong-email",
                "ROLE_PATIENT"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, never())
                .register(any(RegisterRequest.class));
    }

    //✓ 200
    //✓ 401
    //✓ Validation
    //and....
}
