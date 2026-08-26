package com.healthcare.healthcare_api.personnel;

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

@Tag(name = ApiDescription.PERSONNEL, description = ApiDescription.PERSONNEL_API)
@RestController
@RequestMapping("/api/v1/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @Operation(summary = ApiSummary.NEW_PERSONNEL, description = ApiDescription.NEW_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.PERSONNEL_ALREADY_EXIST)})
    @PreAuthorize("hasAnyAuthority('ADMIN_CREATE')")
    @PostMapping("/staffs/newPersonnel")
    public ResponseEntity<Personnel> addStaff(@Valid @RequestBody PersonnelRequest personnelRequest) {
        Personnel personnel = personnelService.addPersonnel(personnelRequest);
        return ResponseEntity.ok(personnel);
    }

    @Operation(summary = ApiSummary.EDIT_PERSONNEL, description = ApiDescription.EDIT_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('PERSONNEL_UPDATE')")
    @PutMapping("/staffs/editPersonnel/{id}")
    public ResponseEntity<Personnel> editStaff(@PathVariable Long id, @Valid @RequestBody PersonnelRequest personnelRequest) {
        Personnel personnel = personnelService.editPersonnel(id, personnelRequest);
        return ResponseEntity.ok(personnel);
    }

    @Operation(summary = ApiSummary.FIND_PERSONNEL, description = ApiDescription.FIND_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'PERSONNEL_READ', 'DOCTOR_READ')")
    @GetMapping("/staffs/findPersonnel/{id}")
    public ResponseEntity<PersonnelResponse> getStaff(@PathVariable Long id) {
        PersonnelResponse staff = personnelService.getPersonnel(id);
        return ResponseEntity.ok(staff);
    }

    @Operation(summary = ApiSummary.ALL_PERSONNEL, description = ApiDescription.ALL_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAnyAuthority('ADMIN_READ', 'PERSONNEL_READ', 'DOCTOR_READ')")
    @GetMapping("/staffs/allPersonnels")
    public ResponseEntity<List<PersonnelResponse>> allStaffs(@RequestParam(required = false) String firstName,
                                                             @RequestParam(required = false) String lastName) {
        List<PersonnelResponse> staff = personnelService.findAllStaffs(firstName, lastName);
        return ResponseEntity.ok(staff);
    }

    @Operation(summary = ApiSummary.DELETE_PERSONNEL, description = ApiDescription.DELETE_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_DELETED_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @DeleteMapping("/staffs/deletePersonnel/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        personnelService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
