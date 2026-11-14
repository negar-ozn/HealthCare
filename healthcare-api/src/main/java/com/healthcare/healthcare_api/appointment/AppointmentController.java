package com.healthcare.healthcare_api.appointment;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = ApiDescription.APPOINTMENT, description = ApiDescription.APPOINTMENT_API)
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = ApiSummary.NEW_APPOINTMENT, description = ApiDescription.NEW_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.APPOINTMENT_ALREADY_EXIST)})
    @PreAuthorize("hasAnyAuthority('RECEPTIONIST_CREATE','PATIENT_CREATE')")
    @PostMapping("/newAppointment")
    public ResponseEntity<AppointmentResponse> addAppointment(@Valid @RequestBody AppointmentRequest appointmentDto) {
        return ResponseEntity.ok(appointmentService.addAppointment(appointmentDto));
    }

    @Operation(summary = ApiSummary.EDIT_APPOINTMENT, description = ApiDescription.EDIT_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.APPOINTMENT_NOT_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_451, description = ApiDescription.MODIFICATION_NOT_AVAILABLE)})
    @PreAuthorize("hasAnyAuthority('RECEPTIONIST_UPDATE','PATIENT_UPDATE')")
    @PutMapping("/editAppointment/{id}")
    public ResponseEntity<AppointmentResponse> editAppointment(@PathVariable Long id,
                                                               @Valid @RequestBody AppointmentRequest appointmentDto) {
        return ResponseEntity.ok(appointmentService.editAppointment(id, appointmentDto));
    }

    @Operation(summary = ApiSummary.FIND_APPOINTMENT, description = ApiDescription.FIND_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.APPOINTMENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    @GetMapping("/findAppointment/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @Operation(summary = ApiSummary.FIND_APPOINTMENT, description = ApiDescription.FIND_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.APPOINTMENT_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('RECEPTIONIST_READ', 'DOCTOR_READ')")
    @GetMapping("/findAppointment")
    public ResponseEntity<AppointmentResponse> getAppointment(@RequestParam LocalDate appointmentDate,
                                                              @RequestParam String patientNationalId) {
        return ResponseEntity.ok(appointmentService.getAppointment(appointmentDate, patientNationalId));
    }

    @Operation(summary = ApiSummary.FIND_ALL_APPOINTMENTS, description = ApiDescription.FIND_ALL_APPOINTMENTS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.APPOINTMENTS_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.APPOINTMENTS_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('RECEPTIONIST_READ','PATIENT_READ', 'DOCTOR_READ')")
    @GetMapping("/allAppointments")
    public ResponseEntity<List<AppointmentResponse>> allAppointments(@RequestParam(required = false) Doctor doctor,
                                                                     @RequestParam(required = false) Patient patient,
                                                                     @RequestParam(required = false) LocalDate appointmentDate,
                                                                     @RequestParam(required = false) String nationalId) {
        return ResponseEntity.ok(appointmentService.allAppointments(doctor, patient, appointmentDate, Long.valueOf(nationalId)));
    }

    @Operation(summary = ApiSummary.CANCEL_APPOINTMENT, description = ApiDescription.CANCEL_APPOINTMENT)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.CANCELED_SUCCESS))
    @PreAuthorize("hasAnyAuthority('PATIENT_UPDATE', 'RECEPTIONIST_UPDATE')")
    @PutMapping("/cancelAppointment/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id,
                                                  @Valid @RequestBody CancelAppointmentRequest request) {
        appointmentService.cancelAppointment(id, request.cancellationReason());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.CONFIRM_APPOINTMENT, description = ApiDescription.CONFIRM_APPOINTMENT)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.CONFIRM_SUCCESS))
    @PreAuthorize("hasAuthority('PATIENT_UPDATE')")
    @PutMapping("/confirmAppointment/{id}")
    public ResponseEntity<Void> confirmAppointment(@PathVariable Long id,
                                                   @RequestParam(required = false) AppointmentStatus status) {
        appointmentService.confirmAppointment(id, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.RESCHEDULED_APPOINTMENT, description = ApiDescription.RESCHEDULED_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.RESCHEDULED_APPOINTMENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.RESCHEDULED_APPOINTMENT_NOT_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_451, description = ApiDescription.RESCHEDULED_APPOINTMENT_NOT_AVAILABLE)})
    @PreAuthorize("hasAuthority('PATIENT_UPDATE')")
    @PutMapping("/rescheduledAppointment/{id}")
    public ResponseEntity<AppointmentResponse> rescheduledAppointment(@PathVariable Long id,
                                                                      @Valid @RequestBody AppointmentRequest appointmentRequest) {
        return ResponseEntity.ok(appointmentService.rescheduledAppointment(id, appointmentRequest));
    }

    @Operation(summary = ApiSummary.DELETE_APPOINTMENT, description = ApiDescription.DELETE_APPOINTMENT)
    @ApiResponses(@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DELETE_SUCCESS))
    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping("/deleteAppointment/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.CHECK_IN_APPOINTMENT, description = ApiDescription.CHECK_IN_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.CHECK_IN_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.RESCHEDULED_APPOINTMENT_NOT_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_UPDATE')")
    @PutMapping("/checkIn")
    public ResponseEntity<AppointmentResponse> checkInAppointment(@RequestParam(required = false) String nationalId) {
        return ResponseEntity.ok(appointmentService.checkInAppointment(Long.valueOf(nationalId)));
    }

    @Operation(summary = ApiSummary.NO_SHOW_APPOINTMENT, description = ApiDescription.NO_SHOW_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.NO_SHOW_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.APPOINTMENT_NOT_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_UPDATE')")
    @PutMapping("/noShow/{appointmentDate}/{status}")
    public ResponseEntity<AppointmentResponse> noShowAppointment(@PathVariable LocalDate appointmentDate,
                                                                 @PathVariable String status) {
        return ResponseEntity.ok(appointmentService.noShowAppointment(appointmentDate, AppointmentStatus.valueOf(status)));
    }

    @Operation(summary = ApiSummary.TODAYS_APPOINTMENT, description = ApiDescription.TODAYS_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.TODAYS_APPOINTMENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.BOOKED_APPOINTMENT_NO_FOUND)})
    @PreAuthorize("hasAuthority('RECEPTIONIST_READ')")
    @GetMapping("/todayAppointments/{date}")
    public ResponseEntity<Page<AppointmentResponse>> todayAppointments(@PathVariable LocalDate date, Pageable pageable) {
        Page<AppointmentResponse> appointmentsList = appointmentService.todayAppointments(date, pageable);
        return ResponseEntity.ok(appointmentsList);
    }

    @Operation(summary = ApiSummary.IN_PROGRESS_APPOINTMENT, description = ApiDescription.IN_PROGRESS_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.IN_PROGRESS_APPOINTMENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.RESCHEDULED_APPOINTMENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    @PutMapping("/inProgress")
    public ResponseEntity<AppointmentResponse> inProgressAppointment(@RequestParam(required = false) String nationalId) {
        return ResponseEntity.ok(appointmentService.inProgressAppointment(Long.valueOf(nationalId)));
    }
}
