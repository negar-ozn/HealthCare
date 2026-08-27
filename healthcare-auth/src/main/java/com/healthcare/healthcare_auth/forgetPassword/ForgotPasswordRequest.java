package com.healthcare.healthcare_auth.forgetPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(

        @Email
        @NotBlank
        String email
) {
}
