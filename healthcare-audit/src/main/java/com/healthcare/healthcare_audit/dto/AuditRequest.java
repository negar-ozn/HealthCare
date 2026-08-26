package com.healthcare.healthcare_audit.dto;

import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import com.healthcare.healthcare_audit.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;

@Schema(description = ApiDescription.AUDIT_REQUEST)
@Builder
public record AuditRequest(

        Long userId,
        @NotBlank
        String username,
        @NotNull
        AuditAction action,
        String entityName,
        Long entityId,
        @NonNull
        AuditStatus status,
        String ipAddress,
        @Size(max = 2000)
        String details,
        String emailVerificationToken,
        String passwordRestToken
) {
}
