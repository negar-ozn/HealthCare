package com.healthcare.healthcare_auth.dto;


import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

//Used when access token expired
@Schema(description = ApiDescription.REFRESH_REQUEST)
public record RefreshTokenRequest(

        @NotBlank
        String refreshToken
) {}


