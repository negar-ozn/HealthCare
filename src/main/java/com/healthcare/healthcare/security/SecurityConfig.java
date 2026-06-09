//package com.healthcare.healthcare.security;
//
//import com.healthcare.healthcare.jwt.JwtAuthFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableMethodSecurity
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
////    private final JwtAuthFilter jwtAuthFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilter(HttpSecurity http) {
//        http.authorizeHttpRequests(auth -> auth.requestMatchers(
//                        "/", "/home", "/login", "/register/**", "/css/**", "/js/**", "/images/**").permitAll()
//                .requestMatchers("/admin/**", "/deleteAppointment/", "/deleteDepartment/").hasRole("ADMIN")
//                .requestMatchers("/doctors/**", "/appointments/allAppointments", "/appointments/findAppointment/").hasAnyRole("DOCTOR")
//                .requestMatchers("/staffs/**", "/appointments/allAppointments", "/appointments/findAppointment/").hasAnyRole("STAFF")
//                .requestMatchers("/patients/**", "/appointments/**").hasAnyRole("PATIENT")
//                .anyRequest().authenticated());
//
//        http.formLogin(form -> form
//                .loginPage("/login")
//                .loginProcessingUrl("/perform_login")
//                .defaultSuccessUrl("/dashboard", true)
//                .failureUrl("/login?error=true")
//                .permitAll());
//
//        http.logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout=true")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll());
//
//        http.exceptionHandling(ex -> ex
//                .accessDeniedPage("/access-denied"));
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
//
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        return RoleHierarchyImpl.fromHierarchy("""
//                ROLE_ADMIN > ROLE_DOCTOR
//                ROLE_ADMIN > ROLE_STAFF
//                ROLE_ADMIN > ROLE_PATIENT
//                ROLE_DOCTOR > ROLE_PATIENT
//                ROLE_STAFF > ROLE_PATIENT
//                """);
//    }
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////
////        http.csrf(csrf -> csrf.disable())
////                .cors(Customizer.withDefaults())
////                .sessionManagement(session ->
////                        session.sessionCreationPolicy(
////                                SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers(
////                                "/auth/login",
////                                "/auth/register"
////                        ).permitAll()
////                        .anyRequest().authenticated()
////                )
//////                .addFilterBefore(
//////                        jwtAuthFilter,
//////                        UsernamePasswordAuthenticationFilter.class
//////                )
////        ;
////
////        return http.build();
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(
////            AuthenticationConfiguration config)
////            throws Exception {
////        return config.getAuthenticationManager();
////    }
//}
