package com.healthcare.healthcare_api.department;

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

@Tag(name = ApiDescription.DEPARTMENT, description = ApiDescription.DEPARTMENT_API)
@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = ApiSummary.NEW_DEPARTMENT, description = ApiDescription.NEW_DEPARTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DEPARTMENT_CREATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_409, description = ApiDescription.DEPARTMENT_ALREADY_EXIST)})
    @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
    @PostMapping("/newDepartment")
    public ResponseEntity<DepartmentResponse> addDepartment(@Valid @RequestBody CreateDepartmentRequest createDepartmentRequest) {
        return ResponseEntity.ok(departmentService.addDepartment(createDepartmentRequest));
    }

    @Operation(summary = ApiSummary.EDIT_DEPARTMENT, description = ApiDescription.EDIT_DEPARTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DEPARTMENT_MODIFICATION_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DEPARTMENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
    @PutMapping("/editDepartment/{id}")
    public ResponseEntity<DepartmentResponse> editDepartment(@PathVariable Long id, @Valid @RequestBody UpdateDepartmentRequest updateDepartmentRequest) {
        return ResponseEntity.ok(departmentService.editDepartment(id, updateDepartmentRequest));
    }

    @Operation(summary = ApiSummary.FIND_DEPARTMENT, description = ApiDescription.FIND_DEPARTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DEPARTMENT_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DEPARTMENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @GetMapping("/getDepartment/{id}")
    public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    @Operation(summary = ApiSummary.ALL_DEPARTMENTS, description = ApiDescription.ALL_DEPARTMENTS)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DEPARTMENTS_FOUND_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DEPARTMENTS_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @GetMapping("/allDepartments")
    public ResponseEntity<List<DepartmentResponse>> allDepartments() {
        return ResponseEntity.ok(departmentService.allDepartments());
    }

    @Operation(summary = ApiSummary.DELETE_DEPARTMENT, description = ApiDescription.DELETE_DEPARTMENT)
    @ApiResponses({@ApiResponse(responseCode = ApiResponseCode.CODE_200, description = ApiDescription.DELETE_DEPARTMENT_SUCCESS),
            @ApiResponse(responseCode = ApiResponseCode.CODE_404, description = ApiDescription.DEPARTMENT_NOT_FOUND)})
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
