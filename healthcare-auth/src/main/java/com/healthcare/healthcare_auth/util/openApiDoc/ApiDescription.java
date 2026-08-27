package com.healthcare.healthcare_auth.util.openApiDoc;

public final class ApiDescription {

    public static final String AUTHENTICATION_API = "Authentication APIs";
    public static final String AUTHENTICATION = "Authentication Service";
    public static final String REGISTER = """
            Creates a new user account.
            
            After successful registration:
            
            • Password is encrypted
            • User is saved
            • Verification token is generated
            • Verification email is sent
            """;
    public static final String LOGIN = """
            Authenticates a user.
            
            Returns:
            
            • Access Token
            • Refresh Token
            • Expiration Time
            """;
    public static final String REFRESH_TOKEN = """
            Generates a new access token using
            a valid refresh token.
            """;
    public static final String LOGOUT = """
            Revokes the refresh token and logs
            the user out.
            """;
    public static final String INTROSPECT = """
            Creates connection between
            authentication server and
            authentication resource.
            """;
    public static final String EMAIL_VERIFICATION = """
            Verifies emails sent to users
            """;
    public static final String FORGET_PASSWORD = """
            Sends a forget password email and
            enables users to define new password.
            """;
    public static final String RESET_PASSWORD = """
            Resets the password.
            """;
    public static final String REGISTER_SUCCESS = "User registered successfully";
    public static final String REGISTER_FAILED = "Validation failed";
    public static final String EMAIL_DUPLICATED = "Email already exists";
    public static final String USERNAME_DUPLICATED = "Username already exists";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String INVALID_CREDENTIAL = "Invalid username or password";
    public static final String REFRESH_TOKEN_SUCCESS = "Refresh access token created successfully";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String EMAIL_SUCCESS = "Email sent successfully";
    public static final String EMAIL_INVALID = "Invalid email verification token";
    public static final String EMAIL_EXPIRED = "Email verification token expired";
    public static final String REGISTRATION = "User registration request";
    public static final String REFRESH_REQUEST = "Refresh token request";
    public static final String LOGOUT_REQUEST = "Logout request";
    public static final String LOGIN_REQUEST = "Login request";
    public static final String LOGIN_RESPONSE = "Login response";
    public static final String REGISTER_RESPONSE = "Register response";
    public static final String AUDIT_REQUEST = "Audit request";
    public static final String INTROSPECT_REQUEST = "Token introspection request";
    public static final String INTROSPECT_RESPONSE = "Token introspection response";
    public static final String USER_ID = "User Id";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String EMAIL = "Email address";
    public static final String ACTION = "Action";
    public static final String ENTITY_NAME = "Entity name";
    public static final String ENTITY_ID = "Entity Id";
    public static final String STATUS = "Status";
    public static final String IP_ADDRESS = "IP address";
    public static final String DETAILS = "Details";
    public static final String EMAIL_VERIFICATION_TOKEN = "Email verification token";
    public static final String PASSWORD_REST_TOKEN = "Password rest token";

}
