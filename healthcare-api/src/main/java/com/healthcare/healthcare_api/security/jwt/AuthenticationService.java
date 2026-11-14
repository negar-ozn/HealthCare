package com.healthcare.healthcare_api.security.jwt;

public interface AuthenticationService {

    AuthenticatedUser authenticate(String token);
}
