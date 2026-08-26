package com.healthcare.healthcare_audit.service;

import com.healthcare.healthcare_audit.dto.AuditRequest;
import com.healthcare.healthcare_audit.dto.AuditResponse;
import com.healthcare.healthcare_audit.entity.AuditLog;
import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import com.healthcare.healthcare_audit.exception.NoSuchAuditLogFoundException;
import com.healthcare.healthcare_audit.kafka.event.*;
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
                .details(event.detail())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createUserLogoutLog(UserLogoutEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.detail())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createUserLoginLog(UserLoginEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.detail())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createRefreshTokenLog(RefreshTokenEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.detail())
                .build();
        auditRepository.save(auditLog);

    }

    @Override
    public void createPasswordResetLog(PasswordResetEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.detail())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createAppointmentBookedLog(AppointmentBookedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createAppointmentCanceledLog(AppointmentCanceledEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createAppointmentCompletedLog(AppointmentCompletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createAppointmentReadLog(AppointmentReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createAppointmentUpdatedLog(AppointmentUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createDepartmentCreatedLog(DepartmentCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createDepartmentReadLog(DepartmentReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createDepartmentUpdatedLog(DepartmentUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createDoctorCreatedLog(DoctorCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createdDoctorDeletedLog(DoctorDeletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createDoctorReadLog(DoctorReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createDoctorUpdatedLog(DoctorUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createMedicalRecordCompletedLog(MedicalRecordCompletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createMedicalRecordCreatedLog(MedicalRecordCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createMedicalRecordReadLog(MedicalRecordReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createMedicalRecordUpdatedLog(MedicalRecordUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPatientCreatedLog(PatientCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPatientDeletedLog(PatientDeletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPatientReadLog(PatientReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPatientUpdatedLog(PatientUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPaymentCreatedLog(PaymentCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPaymentReadLog(PaymentReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPersonnelCreatedLog(PersonnelCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPersonnelDeletedLog(PersonnelDeletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPersonnelReadLog(PersonnelReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createPersonnelUpdatedLog(PersonnelUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
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
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createScheduleCreatedLog(ScheduleCreatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createScheduleUpdatedLog(ScheduleUpdatedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createScheduleReadLog(ScheduleReadEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
                .details(event.details())
                .build();
        auditRepository.save(auditLog);
    }

    @Override
    public void createScheduleDeletedLog(ScheduleDeletedEvent event) {

        AuditLog auditLog = AuditLog.builder()
                .userId(event.userId())
                .username(event.username())
                .action(AuditAction.valueOf(event.action()))
                .entityName(event.entityName())
                .timestamp(Instant.now())
                .status(AuditStatus.valueOf(event.status()))
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
