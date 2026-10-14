package com.healthcare.healthcare_api.doctor;

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

@Tag(name = ApiDescription.DOCTOR, description = ApiDescription.DOCTOR_API)
@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = ApiSummary.NEW_DOCTOR, description = ApiDescription.NEW_DOCTOR)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DOCTOR_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.DOCTOR_ALREADY_EXIST)})
    @PreAuthorize("hasAuthority('DOCTOR_CREATE')")
    @PostMapping("/newDoctor")
    public ResponseEntity<DoctorResponse> addDoctor(@Valid @RequestBody DoctorRequest doctorRequest) {
        return ResponseEntity.ok(doctorService.addDoctor(doctorRequest));
    }

    @Operation(summary = ApiSummary.EDIT_DOCTOR, description = ApiDescription.EDIT_DOCTOR)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DOCTOR_MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DOCTOR_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    @PutMapping("/editDoctor/{id}")
    public ResponseEntity<DoctorResponse> editDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequest doctorRequest) {
        return ResponseEntity.ok(doctorService.editDoctor(id, doctorRequest));
    }

    @Operation(summary = ApiSummary.FIND_DOCTOR, description = ApiDescription.FIND_DOCTOR)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DOCTOR_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DOCTOR_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @GetMapping("/getDoctor/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctor(id));
    }

    @Operation(summary = ApiSummary.ALL_DOCTORS, description = ApiDescription.ALL_DOCTORS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DOCTORS_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DOCTORS_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @GetMapping("/allDoctors")
    public ResponseEntity<List<DoctorResponse>> allDoctors(@RequestParam(required = false) String firstname,
                                                           @RequestParam(required = false) String lastname,
                                                           @RequestParam(required = false) String specialization,
                                                           @RequestParam(required = false) Long licenceNumber) {
        return ResponseEntity.ok(doctorService.allDoctors(firstname, lastname, specialization, licenceNumber));
    }

    @Operation(summary = ApiSummary.DELETE_DOCTOR, description = ApiDescription.DELETE_DOCTOR)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DOCTOR_DELETED_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DOCTOR_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DOCTOR_DELETE')")
    @DeleteMapping("/deleteDoctor/{id}")
    public ResponseEntity<Void> DeleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
