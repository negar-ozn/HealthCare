package com.healthcare.healthcare_api.shared.util.openApiDoc;

public final class ApiDescription {

    public static final String APPOINTMENT = "Appointment Service";
    public static final String APPOINTMENT_API = "Appointment APIs";
    public static final String NEW_APPOINTMENT = "Creates a new appointment";
    public static final String CREATION_SUCCESS = "Appointment created successfully.";
    public static final String APPOINTMENT_ALREADY_EXIST = "The appointment already exists.";
    public static final String EDIT_APPOINTMENT = "Edits the booked appointment.";
    public static final String MODIFICATION_SUCCESS = "The appointment modified successfully.";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found.";
    public static final String MODIFICATION_NOT_AVAILABLE = "Appointment is not available.";
    public static final String FIND_APPOINTMENT = "Finds an appointment by id.";
    public static final String FOUND_SUCCESS = "The appointment found successfully.";
    public static final String FIND_ALL_APPOINTMENTS = "Finds all appointments.";
    public static final String APPOINTMENTS_FOUND_SUCCESS = "The appointments found successfully.";
    public static final String APPOINTMENTS_NOT_FOUND = "The appointments not found.";
    public static final String CANCEL_APPOINTMENT = "Cancels an appointment by id.";
    public static final String CANCELED_SUCCESS = "The appointment canceled successfully.";
    public static final String CONFIRM_APPOINTMENT = "Patient confirms the appointment.";
    public static final String CONFIRM_SUCCESS = "The appointment confirmed successfully.";
    public static final String RESCHEDULED_APPOINTMENT = "Reschedules the appointment.";
    public static final String RESCHEDULED_APPOINTMENT_SUCCESS = "The appointment rescheduled successfully.";
    public static final String RESCHEDULED_APPOINTMENT_NOT_FOUND = "The appointment not found.";
    public static final String RESCHEDULED_APPOINTMENT_NOT_AVAILABLE = "The appointment is not available.";
    public static final String DELETE_APPOINTMENT = "Admin deletes an appointment.";
    public static final String DELETE_SUCCESS = "The appointment deleted successfully.";
    public static final String PATIENT_NOT_FOUND = "Patient not found.";
    public static final String CHECK_IN_APPOINTMENT = "Checks in an appointment";
    public static final String CHECK_IN_SUCCESS = "The appointment checked in successfully";
    public static final String NO_SHOW_APPOINTMENT = """
            The receptionist execute this method
            if the patient does not show up.
            """;
    public static final String NO_SHOW_SUCCESS = "The noShow executed successfully.";
    public static final String TODAYS_APPOINTMENT = "Lists today's booked appointments.";
    public static final String TODAYS_APPOINTMENT_SUCCESS = "Today's booked appointments listed successfully.";
    public static final String BOOKED_APPOINTMENT_NO_FOUND = "Booked appointment not found.";
    public static final String IN_PROGRESS_APPOINTMENT = """
            The doctor changed appointment status
            to In progress by visiting a patient.
            """;
    public static final String IN_PROGRESS_APPOINTMENT_SUCCESS = "The inProgress executed successfully.";
    public static final String APPOINTMENT_RESPONSE = "Appointment Response";

    public static final String DEPARTMENT = "Department Service";
    public static final String DEPARTMENT_API = "Department APIs";
    public static final String NEW_DEPARTMENT = "Admin creates new department";
    public static final String DEPARTMENT_CREATION_SUCCESS = "The department created successfully.";
    public static final String DEPARTMENT_ALREADY_EXIST = "The department already exists.";
    public static final String EDIT_DEPARTMENT = "Admin modifies the department.";
    public static final String DEPARTMENT_MODIFICATION_SUCCESS = "The department modified successfully.";
    public static final String DEPARTMENT_NOT_FOUND = "Department not found.";
    public static final String ALL_DEPARTMENTS = "Admin finds all department.";
    public static final String DEPARTMENTS_FOUND_SUCCESS = "Departments found successfully.";
    public static final String DEPARTMENTS_NOT_FOUND = "Departments not found.";
    public static final String FIND_DEPARTMENT = "Admin finds department by id.";
    public static final String DEPARTMENT_FOUND_SUCCESS = "Department found successfully.";
    public static final String DELETE_DEPARTMENT = "Admin deletes department by id.";
    public static final String DELETE_DEPARTMENT_SUCCESS = "Department deleted successfully.";

    public static final String DOCTOR = "Doctor Service.";
    public static final String DOCTOR_API = "Doctor APIs.";
    public static final String NEW_DOCTOR = "Admin creates new doctor.";
    public static final String DOCTOR_CREATION_SUCCESS = "The doctor created successfully.";
    public static final String DOCTOR_ALREADY_EXIST = "The doctor already exists.";
    public static final String EDIT_DOCTOR = "Admin modifies the doctor.";
    public static final String DOCTOR_MODIFICATION_SUCCESS = "Doctor modified successfully.";
    public static final String DOCTOR_NOT_FOUND = "Doctor not found.";
    public static final String FIND_DOCTOR = "Admin finds doctor by id.";
    public static final String DOCTOR_FOUND_SUCCESS = "Doctor found successfully.";
    public static final String ALL_DOCTORS = "Finds all doctors.";
    public static final String DOCTORS_FOUND_SUCCESS = "All doctors found successfully.";
    public static final String DOCTORS_NOT_FOUND = "Doctors not found.";
    public static final String DELETE_DOCTOR = "Admin deletes doctor by id.";
    public static final String DOCTOR_DELETED_SUCCESS = "Doctor deleted successfully.";

    public static final String PATIENT = "Patient Service";
    public static final String PATIENT_API = "Patient APIs";
    public static final String NEW_PATIENT = "Creates a new patient.";
    public static final String PATIENT_CREATION_SUCCESS = "Patient created successfully.";
    public static final String PATIENT_ALREADY_EXIST = "Patient already exists.";
    public static final String EDIT_PATIENT = "Patient modifies their information.";
    public static final String PATIENT_MODIFICATION_SUCCESS = "Patient modified successfully.";
    public static final String FIND_PATIENT = "Admin, doctor, or receptionist find a patient.";
    public static final String PATIENT_FOUND_SUCCESS = "Patient found successfully.";
    public static final String ALL_PATIENTS = "Admin, doctor, or receptionist find all patients.";
    public static final String PATIENTS_FOUND_SUCCESS = "All patients found successfully.";
    public static final String PATIENTS_NOT_FOUND = "Patients not found.";
    public static final String DELETE_PATIENT = "Delete patient by id.";
    public static final String DELETE_PATIENT_SUCCESS = "Patient deleted successfully.";
    public static final String PATIENT_HISTORY = "Retrieves patient history.";
    public static final String HISTORY_FOUND_SUCCESS = "Patient's history found successfully.";
    public static final String HISTORY_NOT_FOUND = "Patient's history not found.";
    public static final String INACTIVE_PATIENT = "Admin inactives patient's profile.";
    public static final String INACTIVE_PATIENT_SUCCESS = "The patient's profile inactive successfully.";

    public static final String PAYMENT = "Payment Service.";
    public static final String PAYMENT_API = "Payment APIs.";
    public static final String NEW_PAYMENT = "Creates a new payment.";
    public static final String PAYMENT_CREATION_SUCCESS = "The payment created successfully.";
    public static final String FIND_PAYMENT = "Finds a payment by id.";
    public static final String PAYMENT_FOUND_SUCCESS = "Payment found successfully.";
    public static final String PAYMENT_NOT_FOUND = "Payment not found.";
    public static final String ALL_PAYMENTS = "Finds all payments.";
    public static final String PAYMENTS_FOUND_SUCCESS = "Payments found successfully.";
    public static final String PAYMENTS_NOT_FOUND = "Payments not found.";

    public static final String PERSONNEL = "Personnel Service.";
    public static final String PERSONNEL_API = "Personnel APIs.";
    public static final String NEW_PERSONNEL = "Creates a new personnel";
    public static final String PERSONNEL_CREATION_SUCCESS = "The personnel created successfully";
    public static final String PERSONNEL_ALREADY_EXIST = "The personnel already exist.";
    public static final String EDIT_PERSONNEL = "Modifies the personnel's information.";
    public static final String PERSONNEL_MODIFICATION_SUCCESS = "Personnel's information modified successfully.";
    public static final String FIND_PERSONNEL = "Finds personnel by id.";
    public static final String PERSONNEL_NOT_FOUND = "Personnel not found.";
    public static final String PERSONNEL_FOUND_SUCCESS = "Personnel found successfully.";
    public static final String ALL_PERSONNEL = "Finds all personnel.";
    public static final String DELETE_PERSONNEL = "Deletes personnel by id.";
    public static final String PERSONNEL_DELETED_SUCCESS = "The personnel deleted successfully.";

    public static final String SCHEDULE = "Schedule Service.";
    public static final String SCHEDULE_API = "Schedule APIs.";
    public static final String NEW_SCHEDULE = "Creates a new schedule.";
    public static final String SCHEDULE_CREATION_SUCCESS = "Schedule created successfully.";
    public static final String SCHEDULE_ALREADY_EXIST = "Schedule already exist.";
    public static final String EDIT_SCHEDULE = "Modifies the Schedule.";
    public static final String SCHEDULE_MODIFICATION_SUCCESS = "Schedule modified successfully.";
    public static final String SCHEDULE_NOT_FOUND = "Schedule not found.";
    public static final String FIND_SCHEDULE = "Finds schedule by id.";
    public static final String SCHEDULE_FOUND_SUCCESS = "Schedule found successfully.";
    public static final String ALL_SCHEDULE = "Finds all schedules.";
    public static final String SCHEDULES_FOUND_SUCCESS = "Schedules found successfully.";
    public static final String SCHEDULES_NOT_FOUND = "Schedules not found.";
    public static final String DELETE_SCHEDULE = "Deletes schedule by id.";
    public static final String SCHEDULE_DELETED_SUCCESS = "Schedule deleted successfully.";
}
