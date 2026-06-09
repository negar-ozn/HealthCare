package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.StaffDto;
import com.healthcare.healthcare.entitiy.Staff;
import com.healthcare.healthcare.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/staffs/newStaff") // By Staff
    public ResponseEntity<Staff> addStaff(@RequestBody StaffDto staffDto) {
        Staff staff = staffService.addStaff(staffDto);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @PutMapping("/staffs/editStaff/{id}") // By Staff
    public ResponseEntity<Staff> editStaff(@PathVariable Long id, @RequestBody StaffDto staffDto) {
        Staff staff = staffService.editStaff(id, staffDto);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @GetMapping("/staffs/findStaff/{id}") // By Staff, Doctor, Admin
    public ResponseEntity<StaffDto> getStaff(@PathVariable Long id) {
        StaffDto staff = staffService.getStaff(id);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @GetMapping("/staffs/allStaffs") // By Staff, Doctor, Admin
    public ResponseEntity<StaffDto> allStaffs(@RequestParam(required = false) String firstName,
                                              @RequestParam(required = false) String lastName) {
        StaffDto staff = staffService.findAllStaffs(firstName, lastName);
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @DeleteMapping("/staffs/deleteStaff/{id}") // By Admin
    public void deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
    }
}
