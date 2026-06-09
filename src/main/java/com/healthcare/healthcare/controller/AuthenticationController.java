package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.UsersDto;
import com.healthcare.healthcare.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/api/Auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<UsersDto> login(String username, String password) {
//
//    }

//    @PostMapping("/logout")
//    public ResponseEntity<UsersDto> logout() {
//
//    }
}
