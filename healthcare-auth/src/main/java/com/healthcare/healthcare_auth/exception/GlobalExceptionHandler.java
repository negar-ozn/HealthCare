package com.healthcare.healthcare_auth.exception;

import com.healthcare.healthcare_auth.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                ErrorCode.USER_NOT_FOUND, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "404", description = "Email not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailNotFound(EmailNotFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                ErrorCode.USER_NOT_FOUND, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                ErrorCode.ROLE_NOT_FOUND, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "404", description = "Token not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenNotFound(TokenNotFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                ErrorCode.TOKEN_NOT_FOUND, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "409", description = "Email already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
                ErrorCode.EMAIL_ALREADY_EXISTS, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "409", description = "Username already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
                ErrorCode.USERNAME_ALREADY_EXISTS, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "423", description = "Locked account",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiErrorResponse> handleLockedAccount(AccountLockedException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.LOCKED.value(), HttpStatus.LOCKED.getReasonPhrase(),
                ErrorCode.ACCOUNT_LOCKED, "Account is locked", request, null);
    }

    @ApiResponse(responseCode = "423", description = "Account disabled",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountDisabled(AccountDisabledException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.LOCKED.value(), HttpStatus.LOCKED.getReasonPhrase(),
                ErrorCode.ACCOUNT_DISABLED, "Account is disabled", request, null);
    }

    @ApiResponse(responseCode = "401", description = "Invalid refresh token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRefreshToken(InvalidRefreshTokenException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.INVALID_REFRESH_TOKEN, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.INVALID_CREDENTIALS, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "401", description = "Bad credentials",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(BadCredentialException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredential(BadCredentialException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.BAD_CREDENTIAL, "Invalid username or password", request, null);
    }

    @ApiResponse(responseCode = "401", description = "Refresh token revoked",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(RefreshTokenRevokedException.class)
    public ResponseEntity<ApiErrorResponse> handleRefreshTokenRevoked(RefreshTokenRevokedException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.REFRESH_TOKEN_REVOKED, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "401", description = "Refresh token expired",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handleRefreshTokenExpired(RefreshTokenExpiredException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.REFRESH_TOKEN_EXPIRED, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(HttpServletRequest request) {

        String message = "Unexpected server error";
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ErrorCode.INTERNAL_SERVER_ERROR, message, request, null);
    }

    @ApiResponse(responseCode = "401", description = "Invalid email verification token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(InvalidEmailVerificationTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidEmailVerificationToken(InvalidEmailVerificationTokenException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "401", description = "Email verification token expired",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(EmailVerificationTokenExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailVerificationTokenExpired(EmailVerificationTokenExpiredException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.EMAIL_VERIFICATION_TOKEN_EXPIRED, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "401", description = "Invalid password reset token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPasswordResetToken(InvalidPasswordResetTokenException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.INVALID_PASSWORD_REST_TOKEN, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "401", description = "Password reset token expired",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(PasswordResetTokenExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handlePasswordResetTokenExpired(PasswordResetTokenExpiredException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.PASSWORD_REST_TOKEN_EXPIRED, ex.getMessage(), request, null);
    }

    @ApiResponse(responseCode = "400", description = "Method argument not valid",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<FieldValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldValidationError(
                        error.getField(),
                        error.getDefaultMessage()))
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ErrorCode.VALIDATION_ERROR, "Validation failed", request, validationErrors);
    }
    @ApiResponse(responseCode = "401", description = "Invalid user role",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRoleException(InvalidRoleException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ErrorCode.INVALID_REFRESH_TOKEN, ex.getMessage(), request, null);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            int status, String error, ErrorCode code, String message,
            HttpServletRequest request, List<FieldValidationError> validationErrors) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .code(code)
                .message(message)
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

}
