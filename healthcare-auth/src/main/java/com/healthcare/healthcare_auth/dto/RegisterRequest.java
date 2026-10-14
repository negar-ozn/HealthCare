package com.healthcare.healthcare_auth.dto;

import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

//Used when user signs up
@Schema(description = ApiDescription.REGISTRATION)
@Builder
public record RegisterRequest(

        @NotBlank @Size(min = 4, max = 50) String username,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Email String email,
        @NotBlank String role
) {}


