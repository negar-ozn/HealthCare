package com.healthcare.healthcare_auth.dto;


import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

//Returned after successful login
@Schema(description = ApiDescription.LOGIN_RESPONSE)
@Builder
public record LoginResponse(

        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
}


