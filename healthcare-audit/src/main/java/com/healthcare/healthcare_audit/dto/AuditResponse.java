package com.healthcare.healthcare_audit.dto;

import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
public record AuditResponse(

        Long userId,
        String username,
        AuditAction action,
        String entityName,
        String email,
        Long entityId,
        AuditStatus status,
        String ipAddress,
        String details,
        String emailVerificationToken,
        @Schema(example = "")
        String passwordRestToken
) {
}
