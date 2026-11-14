package com.healthcare.healthcare_audit.controller;

import com.healthcare.healthcare_audit.dto.AuditRequest;
import com.healthcare.healthcare_audit.dto.AuditResponse;
import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import com.healthcare.healthcare_audit.service.AuditService;
import com.healthcare.healthcare_audit.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_audit.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_audit.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Tag(name = ApiDescription.AUDIT, description = ApiDescription.AUDIT_API)
@RestController
@RequestMapping("/api/v1/auditLog")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = ApiSummary.NEW_AUDIT, description = ApiDescription.NEW_AUDIT)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.AUDIT_SUCCESS))
    @PostMapping("/internal/createAudit")
    public ResponseEntity<Void> createAudit(@Valid @RequestBody AuditRequest request) {
        auditService.createAudit(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.ALL_LOGS, description = ApiDescription.ALL_LOGS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.LOGS_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.LOGS_NOT_FOUND)})
    @GetMapping("/allLogs")
    public ResponseEntity<Page<AuditResponse>> getAll(Pageable pageable) {
        Page<AuditResponse> allLogs = auditService.getAll(pageable);
        return ResponseEntity.ok(allLogs);
    }

    @Operation(summary = ApiSummary.GET_LOGS, description = ApiDescription.GET_LOGS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.LOGS_BY_CONDITION_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.LOGS_BY_CONDITION_NOT_FOUND)})
    @GetMapping("/findLogs")
    public ResponseEntity<Page<AuditResponse>> findLogBy(@RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) AuditAction action,
                                                         @RequestParam(required = false) AuditStatus status,
                                                         @RequestParam(required = false) String entityName,
                                                         @RequestParam(required = false) Long entityId,
                                                         Pageable pageable) {
        Page<AuditResponse> userLog = auditService.getLogs(userId, action, status, entityName, entityId, pageable);
        return ResponseEntity.ok(userLog);
    }

    @Operation(summary = ApiSummary.LOGS_BY_RANGE_DATE, description = ApiDescription.LOGS_BY_RANGE_DATE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.LOGS_BY_RANGE_DATE_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.LOGS_BY_RANGE_DATE_NOT_FOUND)})
    @GetMapping("/dateRange/{from}/{to}")
    public ResponseEntity<Page<AuditResponse>> getBetweenDates(@PathVariable Instant from, @PathVariable Instant to, Pageable pageable) {
        Page<AuditResponse> timestampLog = auditService.getBetweenDates(from, to, pageable);
        return ResponseEntity.ok(timestampLog);
    }

    @Operation(summary = ApiSummary.LOGS_BY_AUDIT_ID, description = ApiDescription.LOGS_BY_AUDIT_ID)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.LOGS_BY_AUDIT_ID_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.LOGS_BY_AUDIT_ID_NOT_FOUND)})
    @GetMapping("/findAuditLog/{id}")
    public ResponseEntity<AuditResponse> getAuditById(@PathVariable Long id) {
        AuditResponse auditRequest = auditService.getById(id);
        return ResponseEntity.ok(auditRequest);
    }
}
