package com.healthcare.healthcare_api.shared.exception;

import com.healthcare.healthcare_api.shared.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyMadeAppointmentException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyMadeAppointment(AlreadyMadeAppointmentException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.APPOINTMENT_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(AppointmentModificationIsUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnavailableAppointmentModification(AppointmentModificationIsUnavailableException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value(),
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.getReasonPhrase(), ErrorCode.UNAVAILABLE_APPOINTMENT_MODIFICATION,
                ex.getMessage(), request, null);
    }


    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleDepartmentAlreadyExists(DepartmentAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.DEPARTMENT_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(DoctorAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleDoctorAlreadyExists(DoctorAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.DOCTOR_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(MedicalRecordAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleMedicalRecordAlreadyExists(MedicalRecordAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.MEDICAL_RECORD_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoSuchAppointmentFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchAppointmentFound(NoSuchAppointmentFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorCode.NO_SUCH_APPOINTMENT_FOUND,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoSuchPatientFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchPatientFound(NoSuchPatientFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorCode.NO_SUCH_PATIENT_FOUND,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoSuchRecordFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchRecordFound(NoSuchRecordFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorCode.NO_SUCH_RECORD_FOUND,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoSuchPersonnelFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchPersonnelFound(NoSuchPersonnelFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorCode.NO_SUCH_PERSONNEL_FOUND,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handlePatientAlreadyExists(PatientAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.PATIENT_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(RescheduledAppointmentException.class)
    public ResponseEntity<ApiErrorResponse> handleRescheduledAppointment(RescheduledAppointmentException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value(),
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.getReasonPhrase(), ErrorCode.UNAVAILABLE_APPOINTMENT_RESCHEDULING,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(ScheduleAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleScheduleAlreadyExist(ScheduleAlreadyExistsException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.SCHEDULE_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(PersonnelAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handlePersonnelAlreadyExist(PersonnelAlreadyExistException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), ErrorCode.PERSONNEL_ALREADY_EXIST,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoBookedAppointmentFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoNoBookedAppointmentFound(NoBookedAppointmentFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorCode.NO_BOOKED_APPOINTMENT_FOUND,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleUnavailableAuthenticationService(AuthenticationServiceException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value(),
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.getReasonPhrase(), ErrorCode.UNAVAILABLE_AUTHENTICATION_SERVICE,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(), ErrorCode.INVALID_TOKEN,
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoSuchInvoiceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchInvoiceFound(NoSuchInvoiceFoundException ex, HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorCode.NO_SUCH_INVOICE_FOUND,
                ex.getMessage(), request, null);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            int status, String error, ErrorCode code, String message,
            HttpServletRequest request, List<FieldValidationError> validationErrors) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .code(code)
                .message(message)
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
