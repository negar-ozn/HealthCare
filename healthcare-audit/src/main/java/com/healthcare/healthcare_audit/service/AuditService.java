package com.healthcare.healthcare_audit.service;

import com.healthcare.healthcare_audit.dto.AuditRequest;
import com.healthcare.healthcare_audit.dto.AuditResponse;
import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import com.healthcare.healthcare_audit.kafka.event.*;
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
    void createUserLogoutLog(UserLogoutEvent event);
    void createUserLoginLog(UserLoginEvent event);
    void createRefreshTokenLog(RefreshTokenEvent event);
    void createPasswordResetLog(PasswordResetEvent event);
    void createAppointmentBookedLog(AppointmentBookedEvent event);
    void createAppointmentCanceledLog(AppointmentCanceledEvent event);
    void createAppointmentCompletedLog(AppointmentCompletedEvent event);
    void createAppointmentReadLog(AppointmentReadEvent event);
    void createAppointmentUpdatedLog(AppointmentUpdatedEvent event);
    void createDepartmentCreatedLog(DepartmentCreatedEvent event);
    void createDepartmentReadLog(DepartmentReadEvent event);
    void createDepartmentUpdatedLog(DepartmentUpdatedEvent event);
    void createDoctorCreatedLog(DoctorCreatedEvent event);
    void createdDoctorDeletedLog(DoctorDeletedEvent event);
    void createDoctorReadLog(DoctorReadEvent event);
    void createDoctorUpdatedLog(DoctorUpdatedEvent event);
    void createMedicalRecordCompletedLog(MedicalRecordCompletedEvent event);
    void createMedicalRecordCreatedLog(MedicalRecordCreatedEvent event);
    void createMedicalRecordReadLog(MedicalRecordReadEvent event);
    void createMedicalRecordUpdatedLog(MedicalRecordUpdatedEvent event);
    void createPatientCreatedLog(PatientCreatedEvent event);
    void createPatientDeletedLog(PatientDeletedEvent event);
    void createPatientReadLog(PatientReadEvent event);
    void createPatientUpdatedLog(PatientUpdatedEvent event);
    void createPaymentCreatedLog(PaymentCreatedEvent event);
    void createPaymentReadLog(PaymentReadEvent event);
    void createPersonnelCreatedLog(PersonnelCreatedEvent event);
    void createPersonnelDeletedLog(PersonnelDeletedEvent event);
    void createPersonnelReadLog(PersonnelReadEvent event);
    void createPersonnelUpdatedLog(PersonnelUpdatedEvent event);
    void createEmailSentEvent(EmailSentEvent event);
    void createScheduleCreatedLog(ScheduleCreatedEvent event);
    void createScheduleUpdatedLog(ScheduleUpdatedEvent event);
    void createScheduleReadLog(ScheduleReadEvent event);
    void createScheduleDeletedLog(ScheduleDeletedEvent event);
}

