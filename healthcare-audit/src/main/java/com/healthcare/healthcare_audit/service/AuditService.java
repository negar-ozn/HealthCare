package com.healthcare.healthcare_audit.service;

import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.event.UserRegisteredEvent;
import com.healthcare.healthcare_audit.dto.AuditRequest;
import com.healthcare.healthcare_audit.dto.AuditResponse;
import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface AuditService {

    Page<AuditResponse> getAll(Pageable pageable);
    Page<AuditResponse> getLogs(Long userId, AuditAction action, AuditStatus status,
                                String entityName, Long entityId, Pageable pageable);
    Page<AuditResponse> getBetweenDates(Instant from, Instant to, Pageable pageable);
    void createAudit(AuditRequest request);
    AuditResponse getById(Long id);
    void createUserRegisteredLog(UserRegisteredEvent event);
    void createEvent(CoreEvent event);
}

