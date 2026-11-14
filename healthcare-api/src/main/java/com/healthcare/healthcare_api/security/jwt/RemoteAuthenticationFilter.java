package com.healthcare.healthcare_api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RemoteAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public RemoteAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Read Authorization Header
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Extract JWT token
        String token = authorizationHeader.substring(7);
        // Call Auth Service
        AuthenticatedUser authenticatedUser = authenticationService.authenticate(token);
        // Create Authentication
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        // Store Authentication in SecurityContext: Now Spring Security knows who the current user is
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Here The request reaches the controller.
        filterChain.doFilter(request, response);
    }
}
