package com.healthcare.healthcare_api.security.jwt;

import com.healthcare.healthcare_api.shared.exception.AuthenticationServiceException;
import com.healthcare.healthcare_api.shared.exception.InvalidTokenException;
import com.healthcare.healthcare_api.shared.feignClient.AuthClient;
import com.healthcare.healthcare_api.shared.tokenIntrospectDto.TokenIntrospectionRequest;
import com.healthcare.healthcare_api.shared.tokenIntrospectDto.TokenIntrospectionResponse;
import feign.FeignException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthClient authClient;

    public AuthenticationServiceImpl(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public AuthenticatedUser authenticate(String token) {

        // Call Auth Service from AuthClient
        TokenIntrospectionResponse introspectionResponse;
        try {
            introspectionResponse = authClient.introspect(new TokenIntrospectionRequest(token));
            // Validate Response
            if (!introspectionResponse.valid()) {
                throw new InvalidTokenException("Invalid access token.");
            }
        } catch (FeignException ex) {
            throw new AuthenticationServiceException("Authentication service unavailable.", ex);
        }
        // Convert Authorities
        Set<GrantedAuthority> authorities =
                introspectionResponse.authorities()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
        // Return AuthenticatedUser
        return AuthenticatedUser.builder()
                .userId(introspectionResponse.userId())
                .username(introspectionResponse.username())
                .email(introspectionResponse.email())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authorities(authorities)
                .build();
    }
}
