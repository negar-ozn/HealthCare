package com.healthcare.healthcare_api.personnel;

import com.healthcare.healthcare_api.security.jwt.AuthenticatedUser;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiDescription;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiResponseCode;
import com.healthcare.healthcare_api.shared.util.openApiDoc.ApiSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.PERSONNEL_ALREADY_EXIST)})
    @Operation(summary = ApiSummary.NEW_PERSONNEL, description = ApiDescription.NEW_PERSONNEL)
    @PreAuthorize("hasAuthority('PERSONNEL_CREATE')")
    @PostMapping("/newPersonnel")
    public ResponseEntity<PersonnelResponse> addPersonnel(@Valid @RequestBody PersonnelRequest personnelRequest) {
        PersonnelResponse response = personnelService.addPersonnel(personnelRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = ApiSummary.EDIT_PERSONNEL, description = ApiDescription.EDIT_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PERSONNEL_UPDATE')")
    @PutMapping("/editPersonnel/{id}")
    public ResponseEntity<PersonnelResponse> editPersonnel(@PathVariable Long id, @Valid @RequestBody PersonnelRequest personnelRequest) {
        return ResponseEntity.ok(personnelService.editPersonnel(id, personnelRequest));
    }

    @Operation(summary = ApiSummary.FIND_PERSONNEL, description = ApiDescription.FIND_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PERSONNEL_READ')")
    @GetMapping("/findPersonnel/{id}")
    public ResponseEntity<PersonnelResponse> findPersonnel(@PathVariable Long id) {
        return ResponseEntity.ok(personnelService.findPersonnel(id));
    }

    @Operation(summary = ApiSummary.ALL_PERSONNEL_BY_ROLE, description = ApiDescription.ALL_PERSONNEL_BY_ROLE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PERSONNEL_READ')")
    @GetMapping("/allPersonnelByRole/{personnelRole}")
    public ResponseEntity<List<PersonnelResponse>> findAllPersonnelByRole(@PathVariable String personnelRole) {
        return ResponseEntity.ok(personnelService.getAllPersonnelByRole(personnelRole));
    }

    @Operation(summary = ApiSummary.ALL_PERSONNEL, description = ApiDescription.ALL_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PERSONNEL_READ')")
    @GetMapping("/allPersonnel")
    public ResponseEntity<List<PersonnelResponse>> findAllPersonnel() {
        return ResponseEntity.ok(personnelService.getAllPersonnel());
    }

    @Operation(summary = ApiSummary.DELETE_PERSONNEL, description = ApiDescription.DELETE_PERSONNEL)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_DELETED_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PERSONNEL_DELETE')")
    @DeleteMapping("/deletePersonnel/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        personnelService.deletePersonnel(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ApiSummary.VIEW_PERSONNEL_PROFILE, description = ApiDescription.VIEW_PERSONNEL_PROFILE)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.PERSONNEL_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.PERSONNEL_NOT_FOUND)})
    @PreAuthorize("hasAuthority('PROFILE_READ_SELF')")
    @GetMapping("/myProfile")
    public ResponseEntity<PersonnelResponse> getMyProfile(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(personnelService.getMyProfile(user.getUserId()));
    }
}
