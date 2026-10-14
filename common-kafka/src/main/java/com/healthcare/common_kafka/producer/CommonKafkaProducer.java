package com.healthcare.common_kafka.producer;

import com.healthcare.common_kafka.event.*;
import com.healthcare.common_kafka.topic.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class CommonKafkaProducer {

    private final KafkaTemplate<String, CoreEvent> coreEventKafkaTemplate;
    private final KafkaTemplate<String, NotificationEvent> notificationEventKafkaTemplate;
    private final KafkaTemplate<String, UserRegisteredEvent> userRegisteredEventKafkaTemplate;


    public CommonKafkaProducer(KafkaTemplate<String, CoreEvent> coreEventKafkaTemplate,
                               KafkaTemplate<String, NotificationEvent> notificationEventKafkaTemplate,
                               KafkaTemplate<String, UserRegisteredEvent> userRegisteredEventKafkaTemplate) {

        this.coreEventKafkaTemplate = coreEventKafkaTemplate;
        this.notificationEventKafkaTemplate = notificationEventKafkaTemplate;
        this.userRegisteredEventKafkaTemplate = userRegisteredEventKafkaTemplate;
    }

    public void publishAppointmentBookedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_BOOKED, event);
    }

    public void publishAppointmentRescheduledEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_RESCHEDULED, event);
    }

    public void publishAppointmentCanceledEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_CANCELED, event);
    }

    public void publishAppointmentCompletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_COMPLETED, event);
    }

    public void publishAppointmentReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_READ, event);
    }

    public void publishAppointmentUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_UPDATED, event);
    }

    public void publishAppointmentDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_DELETED, event);
    }

    public void publishAppointmentConfirmedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_CONFIRMED, event);
    }

    public void publishAppointmentCheckInEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_CHECKED_IN, event);
    }

    public void publishAppointmentNoShowEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_NO_SHOWED, event);
    }

    public void publishAppointmentInProgressEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.APPOINTMENT_IN_PROGRESS, event);
    }

    public void publishDepartmentCreatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DEPARTMENT_CREATED, event);
    }

    public void publishDepartmentReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DEPARTMENT_READ, event);
    }

    public void publishDepartmentUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DEPARTMENT_UPDATED, event);
    }

    public void publishDepartmentDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DEPARTMENT_DELETED, event);
    }

    public void publishDoctorCreatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DOCTOR_CREATED, event);
    }

    public void publishDoctorDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DOCTOR_DELETED, event);
    }

    public void publishDoctorReadProducer(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DOCTOR_READ, event);
    }

    public void publishDoctorUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.DOCTOR_UPDATED, event);
    }

    public void publishMedicalRecordCompletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.MEDICAL_RECORD_COMPLETED, event);
    }

    public void publishMedicalRecordCreatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.MEDICAL_RECORD_CREATED, event);
    }

    public void publishMedicalRecordReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.MEDICAL_RECORD_READ, event);
    }

    public void publishMedicalRecordUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.MEDICAL_RECORD_UPDATED, event);
    }

    public void publishMedicalRecordDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.MEDICAL_RECORD_DELETED, event);
    }

    public void publishPatientCreatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PATIENT_CREATED, event);
    }

    public void publishPatientDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PATIENT_DELETED, event);
    }

    public void publishPatientReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PATIENT_READ, event);
    }

    public void publishPatientUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PATIENT_UPDATED, event);
    }

    public void publishPaymentCreatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PAYMENT_CREATED, event);
    }

    public void publishPaymentReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PAYMENT_READ, event);
    }

    public void publishPersonnelCreatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PERSONNEL_CREATED, event);
    }

    public void publishPersonnelUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PERSONNEL_UPDATED, event);
    }

    public void publishPersonnelDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PERSONNEL_DELETED, event);
    }

    public void publishPersonnelReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PERSONNEL_READ, event);
    }

    public void publishProfileReadSelfEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.PROFILE_READ_SELF, event);
    }

    public void publishScheduleCreatedEvent(CoreEvent event){
        coreEventKafkaTemplate.send(KafkaTopics.SCHEDULE_CREATED, event);
    }

    public void publishScheduleDeletedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.SCHEDULE_DELETED, event);
    }

    public void publishScheduleReadEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.SCHEDULE_READ, event);
    }

    public void publishScheduleUpdatedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.SCHEDULE_UPDATED, event);
    }

    // sends email verification
    public void publishNotificationEvent(NotificationEvent event) {
        notificationEventKafkaTemplate.send(KafkaTopics.NOTIFICATION_EVENTS, event.correlationId(), event);
    }

    // email sent event
    public void publishEmailSentEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.EMAIL_SENT, event);
    }

    // email verified event
    public void publishEmailVerifiedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.EMAIL_VERIFIED, event);
    }

    // sends welcome email
    public void publishWelcomeEvent(NotificationEvent event) {
        notificationEventKafkaTemplate.send(KafkaTopics.WELCOME_EMAIL, event);
    }

    // sends email forget password
    public void publishPasswordResetEmailEvent(NotificationEvent event) {
        notificationEventKafkaTemplate.send(KafkaTopics.PASSWORD_RESET, event);
    }

    public void publishPersonnelPasswordSetupNotificationEvent(NotificationEvent event) {
        notificationEventKafkaTemplate.send(KafkaTopics.PERSONNEL_PASSWORD_SETUP, event);
    }

    // email forget password sent event
    public void publishForgetPasswordEmailSentEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.FORGET_PASSWORD_EMAIL, event);
    }

    // new password submitted event
    public void publishNewPasswordSubmittedEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.NEW_PASSWORD, event);
    }

    public void publishRefreshTokenEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.REFRESH_TOKEN, event);
    }

    public void publishIntrospectEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.INTROSPECT, event);
    }

    public void publishUserLoginEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.USER_LOGIN, event);
    }

    public void publishUserLogoutEvent(CoreEvent event) {
        coreEventKafkaTemplate.send(KafkaTopics.USER_LOGOUT, event);
    }

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        userRegisteredEventKafkaTemplate.send(KafkaTopics.USER_REGISTERED, event.email(), event);
    }
}
