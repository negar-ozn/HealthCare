package com.healthcare.healthcare_api.personnel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
public record PersonnelRequest(

        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthdate,
        @NotBlank String gender,
        @NotNull String nationalId,
        @NotBlank String phone,
        @NotBlank @Email String email,
        @NotBlank String address,
        @NotBlank String role,
        @NotNull Long departmentId
) {
}
