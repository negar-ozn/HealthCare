package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.enums.UserState;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 5, message = "Username must be at least 5 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 7, message = "Message must be at least 7 characters")
    private String password;

    @NotBlank(message = "Enter the password again")
    private String confirmPassword;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    private UserState userState;
}
