package com.healthcare.healthcare_api.security.config;

import com.healthcare.healthcare_api.security.jwt.JwtAccessDeniedHandler;
import com.healthcare.healthcare_api.security.jwt.JwtAuthenticationEntryPoint;
import com.healthcare.healthcare_api.security.jwt.RemoteAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RemoteAuthenticationFilter remoteAuthenticationFilter;

    public SecurityConfig(JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, RemoteAuthenticationFilter remoteAuthenticationFilter) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.remoteAuthenticationFilter = remoteAuthenticationFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilter(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(ex ->
                ex.accessDeniedPage("/access-denied")
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint));
        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/", "/home", "/v1/api/auth/login", "/register/**", "/css/**",
                        "/js/**", "/images/**", "/actuator/health").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/admin/**", "/deleteAppointment/", "/deleteDepartment/").hasRole("ADMIN")
                .requestMatchers("/doctors/**", "/appointments/allAppointments", "/appointments/findAppointment/")
                .hasAnyRole("DOCTOR")
                .requestMatchers("/staffs/**", "/appointments/allAppointments", "/appointments/findAppointment/")
                .hasAnyRole("STAFF")
                .requestMatchers("/patients/**", "/appointments/**").hasAnyRole("PATIENT")
                .anyRequest().authenticated());
        http.addFilterBefore(remoteAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_DOCTOR
                ROLE_ADMIN > ROLE_STAFF
                ROLE_ADMIN > ROLE_PATIENT
                ROLE_DOCTOR > ROLE_PATIENT
                ROLE_STAFF > ROLE_PATIENT
                """);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}
