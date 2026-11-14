package com.healthcare.healthcare_auth.controller;

import com.healthcare.healthcare_auth.dto.PasswordSetupRequest;
import com.healthcare.healthcare_auth.service.PasswordSetupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class PasswordSetupController {

    private final PasswordSetupService passwordSetupService;

    public PasswordSetupController(PasswordSetupService passwordSetupService) {
        this.passwordSetupService = passwordSetupService;
    }

    @PostMapping("/password-setup")
    public ResponseEntity<Void> setupPassword(@Valid @RequestBody PasswordSetupRequest request) {
        passwordSetupService.setupPassword(request);
        return ResponseEntity.noContent().build();
    }
}
