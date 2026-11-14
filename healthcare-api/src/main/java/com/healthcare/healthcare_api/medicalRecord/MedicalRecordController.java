package com.healthcare.healthcare_api.medicalRecord;

import com.healthcare.healthcare_api.appointment.AppointmentResponse;
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

@Tag(name = ApiDescription.MEDICAL_RECORD, description = ApiDescription.MEDICAL_RECORD_API)
@RestController
@RequestMapping("/api/v1/medicalRecords")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @Operation(summary = ApiSummary.NEW_MEDICAL_RECORD, description = ApiDescription.NEW_MEDICAL_RECORD)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.MEDICAL_RECORD_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.MEDICAL_RECORD_ALREADY_EXIST)})
    @PreAuthorize("hasAuthority('DOCTOR_CREATE')")
    @PostMapping("medicalRecord/newMedicalRecord")
    public ResponseEntity<MedicalRecordResponse> addMedicalRecord(@Valid @RequestBody MedicalRecordRequest medicalRecordRequest) {
        return ResponseEntity.ok(medicalRecordService.addMedicalRecord(medicalRecordRequest));
    }

    @Operation(summary = ApiSummary.EDIT_MEDICAL_RECORD, description = ApiDescription.EDIT_MEDICAL_RECORD)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.MEDICAL_RECORD_NOT_FOUND),
            @ApiResponse(responseCode = ApiResponseCode.CODE_451, description = ApiDescription.MEDICAL_RECORD_MODIFICATION_NOT_AVAILABLE)})
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    @PutMapping("medicalRecord/editMedicalRecord/{id}")
    public ResponseEntity<MedicalRecordResponse> editMedicalRecord(@PathVariable Long id,
                                                                   @Valid @RequestBody MedicalRecordRequest medicalRecordRequest) {
        return ResponseEntity.ok(medicalRecordService.editMedicalRecord(id, medicalRecordRequest));
    }

    @Operation(summary = ApiSummary.FIND_MEDICAL_RECORD, description = ApiDescription.FIND_MEDICAL_RECORD)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.MEDICAL_RECORD_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.MEDICAL_RECORD_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @GetMapping("medicalRecord/getMedicalRecord/{id}")
    public ResponseEntity<MedicalRecordResponse> getMedicalRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecord(id));
    }

    @Operation(summary = ApiSummary.FIND_ALL_MEDICAL_RECORDS, description = ApiDescription.FIND_ALL_MEDICAL_RECORDS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.MEDICAL_RECORDS_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.MEDICAL_RECORDS_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @GetMapping("medicalRecord/allMedicalRecord")
    public ResponseEntity<List<MedicalRecordResponse>> allMedicalRecords(@RequestParam(required = false) Long nationalId) {
        return ResponseEntity.ok(medicalRecordService.allMedicalRecord(nationalId));
    }

    @Operation(summary = ApiSummary.DELETE_MEDICAL_RECORD, description = ApiDescription.DELETE_MEDICAL_RECORD)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.MEDICAL_RECORD_DELETE_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.MEDICAL_RECORD_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_DELETE')")
    @DeleteMapping("medicalRecord/deleteMedicalRecord/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.COMPLETE_APPOINTMENT, description = ApiDescription.COMPLETE_APPOINTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.COMPLETE_APPOINTMENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.APPOINTMENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    @PutMapping("/submitMedicalRecord/{id}")
    public ResponseEntity<AppointmentResponse> submitMedicalRecord(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.submitMedicalRecord(id));
    }
}
