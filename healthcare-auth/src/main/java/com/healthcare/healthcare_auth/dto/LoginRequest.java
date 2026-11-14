package com.healthcare.healthcare_auth.dto;

import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = ApiDescription.LOGIN_REQUEST)
public record LoginRequest(

        @NotBlank String username,
        @NotBlank String password
) {
}


