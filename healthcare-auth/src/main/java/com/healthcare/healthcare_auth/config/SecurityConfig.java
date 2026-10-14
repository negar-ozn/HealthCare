package com.healthcare.healthcare_auth.config;

import com.healthcare.healthcare_auth.security.InternalServiceAuthenticationFilter;
import com.healthcare.healthcare_auth.security.JwtAccessDeniedHandler;
import com.healthcare.healthcare_auth.security.JwtAuthenticationEntryPoint;
import com.healthcare.healthcare_auth.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler, InternalServiceAuthenticationFilter internalServiceAuthenticationFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.internalServiceAuthenticationFilter = internalServiceAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) {

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);


        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.authorizeHttpRequests(auth -> auth
                //users don't have JWT yet, so all users can access these public endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers(
                        "/api/v1/auth/register",
                        "/api/v1/auth/verifyEmail",
                        "/api/v1/auth/login",
                        "/api/v1/auth/logout",
                        "/api/v1/auth/forgetPassword",
                        "/api/v1/auth/resetPassword",
                        "/api/v1/auth/setNewPassword",
                        "/api/v1/auth/introspect",
                        "/api/v1/auth/password-setup")
                .permitAll()
                // for protected endpoints which require authentication
                .anyRequest().authenticated()
        );
        http.exceptionHandling(ex ->
                ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));
        http.addFilterBefore(internalServiceAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
