package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.DepartmentDto;
import com.healthcare.healthcare.entitiy.Department;
import com.healthcare.healthcare.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/v1/api/Departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    //All services run by Admin!!

    @PostMapping("/newDepartment")
    public ResponseEntity<Department> addDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        Department department = departmentService.addDepartment(departmentDto);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PutMapping("/editDepartment/{id}")
    public ResponseEntity<Department> editDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDto departmentDto) {
        Department department = departmentService.editDepartment(id, departmentDto);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @GetMapping("/getDepartment/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
        DepartmentDto department = departmentService.getDepartment(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @GetMapping("/allDepartments")
    public ResponseEntity<List<DepartmentDto>> allDepartments(@RequestParam(required = false) String name) {
        List<DepartmentDto> department = departmentService.allDepartments(name);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
