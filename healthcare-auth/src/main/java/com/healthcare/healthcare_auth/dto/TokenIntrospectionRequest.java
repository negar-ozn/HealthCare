package com.healthcare.healthcare_auth.dto;


import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = ApiDescription.INTROSPECT_REQUEST)
public record TokenIntrospectionRequest(

        @NotBlank String accessToken
) {}



