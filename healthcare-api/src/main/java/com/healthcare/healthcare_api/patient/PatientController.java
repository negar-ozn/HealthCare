package com.healthcare.healthcare_api.patient;

import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = ApiDescription.PATIENT, description = ApiDescription.PATIENT_API)
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = ApiSummary.NEW_PATIENT, description = ApiDescription.NEW_PATIENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PATIENT_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.PATIENT_ALREADY_EXIST)})
    @PreAuthorize("hasAuthority('PATIENT_CREATE')")
    @PostMapping("/patients/newPatient")
    public ResponseEntity<PatientResponse> addPatient(@Valid @RequestBody PatientRequest patientRequest) {
        return ResponseEntity.ok(patientService.addPatient(patientRequest));
    }

    @Operation(summary = ApiSummary.EDIT_PATIENT, description = ApiDescription.EDIT_PATIENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PATIENT_MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PATIENT_UPDATE')")
    @PutMapping("/patients/editPatient/{id}")
    public ResponseEntity<PatientResponse> editPatient(@PathVariable Long id, @Valid @RequestBody PatientRequest patientRequest) {
        return ResponseEntity.ok(patientService.editPatient(id, patientRequest));
    }

    @Operation(summary = ApiSummary.FIND_PATIENT, description = ApiDescription.FIND_PATIENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PATIENT_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENT_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('DOCTOR_READ', 'RECEPTIONIST_READ')")
    @GetMapping("/patients/getPatient/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @Operation(summary = ApiSummary.ALL_PATIENTS, description = ApiDescription.ALL_PATIENTS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PATIENTS_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENTS_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('DOCTOR_READ', 'RECEPTIONIST_READ')")
    @GetMapping("/patients/allPatients")
    public ResponseEntity<List<PatientResponse>> allPatient(@RequestParam(required = false) String firstName,
                                                            @RequestParam(required = false) String lastName,
                                                            @RequestParam(required = false) Long nationalId) {
        return ResponseEntity.ok(patientService.allPatient(firstName, lastName, nationalId));
    }

    @Operation(summary = ApiSummary.DELETE_PATIENT, description = ApiDescription.DELETE_PATIENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DELETE_PATIENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PATIENT_DELETE')")
    @DeleteMapping("/patients/deletePatient/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.PATIENT_HISTORY, description = ApiDescription.PATIENT_HISTORY)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.HISTORY_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.HISTORY_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('DOCTOR_READ', 'RECEPTIONIST_READ')")
    @GetMapping("/patients/history")
    public ResponseEntity<List<PatientResponse>> patientHistory(@RequestParam(required = false) Long nationalId,
                                                                @RequestParam(required = false) Long id) {
        return ResponseEntity.ok(patientService.patientHistory(id, nationalId));
    }

    @Operation(summary = ApiSummary.INACTIVE_PATIENT, description = ApiDescription.INACTIVE_PATIENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.INACTIVE_PATIENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PATIENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PATIENT_UPDATE')")
    @PutMapping("/patient/inactivePatient/{id}")
    public ResponseEntity<Void> inactivePatient(@PathVariable Long id) {
        patientService.inactivate(id);
        return ResponseEntity.noContent().build();
    }
}
