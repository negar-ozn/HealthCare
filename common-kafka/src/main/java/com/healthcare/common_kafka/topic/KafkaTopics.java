package com.healthcare.common_kafka.topic;

public class KafkaTopics {

    private KafkaTopics() {
    }

    // Patient
    public static final String PATIENT_CREATED = "patient-created";
    public static final String PATIENT_UPDATED = "patient-updated";
    public static final String PATIENT_DELETED = "patient-deleted";
    public static final String PATIENT_READ = "patient-read";

    // Doctor
    public static final String DOCTOR_CREATED = "doctor-created";
    public static final String DOCTOR_UPDATED = "doctor-updated";
    public static final String DOCTOR_DELETED = "doctor-deleted";
    public static final String DOCTOR_READ = "doctor-read";

    // Appointment
    public static final String APPOINTMENT_BOOKED = "appointment-booked";
    public static final String APPOINTMENT_UPDATED = "appointment-updated";
    public static final String APPOINTMENT_CANCELED = "appointment-canceled";
    public static final String APPOINTMENT_COMPLETED = "appointment-completed";
    public static final String APPOINTMENT_READ = "appointment-read";

    // Department
    public static final String DEPARTMENT_CREATED = "department-created";
    public static final String DEPARTMENT_UPDATED = "department-updated";
    public static final String DEPARTMENT_READ = "department-read";

    // Medical Record
    public static final String MEDICAL_RECORD_CREATED = "medical-record-created";
    public static final String MEDICAL_RECORD_UPDATED = "medical-record-updated";
    public static final String MEDICAL_RECORD_COMPLETED = "medical-record-completed";
    public static final String MEDICAL_RECORD_READ = "medical-record-read";

    // Payment
    public static final String PAYMENT_CREATED = "payment-created";
    public static final String PAYMENT_READ = "payment-read";

    // Personnel
    public static final String PERSONNEL_CREATED = "personnel-created";
    public static final String PERSONNEL_UPDATED = "personnel-updated";
    public static final String PERSONNEL_DELETED = "personnel-deleted";
    public static final String PERSONNEL_READ = "personnel-read";

    // Schedule
    public static final String SCHEDULE_CREATED = "schedule-created";
    public static final String SCHEDULE_UPDATED = "schedule-updated";
    public static final String SCHEDULE_DELETED = "schedule-deleted";
    public static final String SCHEDULE_READ = "schedule-read";

    // Authentication
    public static final String USER_REGISTERED = "user-registered";
    public static final String USER_LOGIN = "user-login";
    public static final String USER_LOGOUT = "user-logout";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String INTROSPECT = "introspect";
    public static final String PASSWORD_RESET = "password-reset";
    public static final String NEW_PASSWORD = "new-password";

    // Notification
    public static final String NOTIFICATION_EVENTS = "notification-events";
    public static final String WELCOME_EMAIL = "welcome-email";
    public static final String EMAIL_SENT = "email-sent";
    public static final String EMAIL_VERIFIED = "email-verified";
    public static final String FORGET_PASSWORD_EMAIL = "forget-password-email";
}
