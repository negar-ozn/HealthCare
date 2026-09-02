package com.healthcare.healthcare_audit.service;

import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.event.EmailSentEvent;
import com.healthcare.common_kafka.event.UserRegisteredEvent;
import com.healthcare.healthcare_audit.dto.AuditRequest;
import com.healthcare.healthcare_audit.dto.AuditResponse;
import com.healthcare.healthcare_audit.entity.AuditLog;
import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import com.healthcare.healthcare_audit.exception.NoSuchAuditLogFoundException;
import com.healthcare.healthcare_audit.repository.AuditRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public Page<AuditResponse> getAll(Pageable pageable) {
        Page<AuditLog> auditLogs = auditRepository.findAll(pageable);
        if (auditLogs.isEmpty()) {
            throw new NoSuchAuditLogFoundException("Audit log not found.");
        }
        return auditLogMapper(auditLogs);
    }

    @Override
    public Page<AuditResponse> getLogs(Long userId, AuditAction action, AuditStatus status,
                                       String entityName, Long entityId, Pageable pageable) {
        Page<AuditLog> auditLogs = auditRepository.findByUserIdOrActionOrStatusOrEntityNameOrEntityId(userId, action, status,
                entityName, entityId, pageable);
        if (auditLogs.isEmpty()) {
            throw new NoSuchAuditLogFoundException("Audit log not found.");
        }
        return auditLogMapper(auditLogs);
    }

    @Override
    public Page<AuditResponse> getBetweenDates(Instant from, Instant to, Pageable pageable) {
        Page<AuditLog> auditLogs = auditRepository.findByTimestampBetween(from, to, pageable);
        if (auditLogs.isEmpty()) {
            throw new NoSuchAuditLogFoundException("Audit log not found.");
        }
        return auditLogMapper(auditLogs);
    }

    @Override
    public void createAudit(AuditRequest request) {

        AuditLog auditLog = AuditLog.builder()
                .userId(request.userId())
                .username(request.username())
                .action(request.action())
                .entityName(request.entityName())
                .entityId(request.entityId())
                .timestamp(Instant.now())
                .ipAddress(request.ipAddress())
                .status(request.status())
                .details(request.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public AuditResponse getById(Long id) {
        Optional<AuditLog> auditLogs = auditRepository.findById(id);
        if (auditLogs.isEmpty()) {
            throw new NoSuchAuditLogFoundException("Audit log not found.");
        }
        AuditLog auditLogInfo = auditLogs.get();
        return AuditResponse.builder()
                .userId(auditLogInfo.getUserId())
                .username(auditLogInfo.getUsername())
                .action(auditLogInfo.getAction())
                .entityName(auditLogInfo.getEntityName())
                .entityId(auditLogInfo.getEntityId())
                .status(auditLogInfo.getStatus())
                .ipAddress(auditLogInfo.getIpAddress())
                .details(auditLogInfo.getDetails())
                .build();
    }

    @Override
    public void createUserRegisteredLog(UserRegisteredEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .email(event.email())
                .verificationLink(event.verificationLink())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .eventType(event.eventType())
                .details(event.detail())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createEmailSentEvent(EmailSentEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .email(event.email())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .eventType(event.eventType())
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createEvent(CoreEvent event) {
        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(String.valueOf(event.entityName()))
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .eventType(event.eventType().name())
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    private Page<AuditResponse> auditLogMapper(Page<AuditLog> auditLog) {

        return auditLog.map(logInfo -> AuditResponse.builder()
                .userId(logInfo.getUserId())
                .username(logInfo.getUsername())
                .action(logInfo.getAction())
                .email(logInfo.getEmail())
                .entityName(logInfo.getEntityName())
                .entityId(logInfo.getEntityId())
                .status(logInfo.getStatus())
                .ipAddress(logInfo.getIpAddress())
                .details(logInfo.getDetails())
                .build());
    }

}
