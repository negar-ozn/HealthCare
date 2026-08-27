package com.healthcare.healthcare_auth.controller;

import com.healthcare.healthcare_auth.dto.*;
import com.healthcare.healthcare_auth.emailVerification.EmailVerificationService;
import com.healthcare.healthcare_auth.exception.ApiErrorResponse;
import com.healthcare.healthcare_auth.forgetPassword.ForgetPasswordService;
import com.healthcare.healthcare_auth.forgetPassword.ForgotPasswordRequest;
import com.healthcare.healthcare_auth.forgetPassword.ResetPasswordRequest;
import com.healthcare.healthcare_auth.service.AuthenticationService;
import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_auth.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_auth.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = ApiDescription.AUTHENTICATION, description = ApiDescription.AUTHENTICATION_API)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final EmailVerificationService emailVerificationService;
    private final ForgetPasswordService forgetPasswordService;

    public AuthenticationController(AuthenticationService authService, EmailVerificationService emailVerificationService, ForgetPasswordService forgetPasswordService) {
        this.authService = authService;
        this.emailVerificationService = emailVerificationService;
        this.forgetPasswordService = forgetPasswordService;
    }

    //    GET  /auth/profile
    @Operation(summary = ApiSummary.REGISTER, description = ApiDescription.REGISTER)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_201, description = ApiDescription.REGISTER_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_400, description = ApiDescription.REGISTER_FAILED,
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.EMAIL_DUPLICATED,
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.USERNAME_DUPLICATED,
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))})
    @PostMapping("/register") // Used when user signs up
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = ApiSummary.LOGIN, description = ApiDescription.LOGIN)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.LOGIN_SUCCESS))
//            @ApiResponse(responseCode = ApiResponseCode.CODE_401, description = ApiDescription.INVALID_CREDENTIAL)})
    @PostMapping("/login") //Used during login
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshAccessToken") //Used when access token expired
    @Operation(summary = ApiSummary.REFRESH_TOKEN, description = ApiDescription.REFRESH_TOKEN)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.REFRESH_TOKEN_SUCCESS))
    public ResponseEntity<LoginResponse> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout") //when user logs out
    @Operation(summary = ApiSummary.LOGOUT, description = ApiDescription.LOGOUT)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.LOGOUT_SUCCESS))
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/introspect")
    @Operation(summary = ApiSummary.INTROSPECT, description = ApiDescription.INTROSPECT)
    public TokenIntrospectionResponse introspect(@Valid @RequestBody TokenIntrospectionRequest request) {
        return authService.introspect(request);
    }

    @GetMapping("/verifyEmail")
    @Operation(summary = ApiSummary.EMAIL_VERIFICATION, description = ApiDescription.EMAIL_VERIFICATION)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.EMAIL_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_401, description = ApiDescription.EMAIL_INVALID),
            @ApiResponse(responseCode = ApiResponseCode.CODE_401, description = ApiDescription.EMAIL_EXPIRED)})
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        emailVerificationService.verify(token);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/forgotPassword")
    @Operation(summary = ApiSummary.FORGET_PASSWORD, description = ApiDescription.FORGET_PASSWORD)
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        forgetPasswordService.forgotPassword(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    @Operation(summary = ApiSummary.REST_PASSWORD, description = ApiDescription.RESET_PASSWORD)
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        forgetPasswordService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

}
