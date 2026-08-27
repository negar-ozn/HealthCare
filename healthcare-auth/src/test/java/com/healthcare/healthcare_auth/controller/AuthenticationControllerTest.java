package com.healthcare.healthcare_auth.controller;

import com.healthcare.healthcare_auth.dto.RegisterRequest;
import com.healthcare.healthcare_auth.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    AuthenticationService authenticationService;

    @Test
    void shouldRegisterSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "negar@gmail.com",
                "Password@123",
                "ROLE_PATIENT"
        );

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
        verify(authenticationService).register(any(RegisterRequest.class));

    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "negar",
                "wrong-email",
                "Password@123",
                "ROLE_PATIENT"
        );

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    //✓ 200
    //✓ 401
    //✓ Validation
    //and....
}
