package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.DoctorDto;
import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/v1/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    //By Admin
    @PostMapping("/doctor/newDoctor")
    public ResponseEntity<Doctor> addDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorService.addDoctor(doctorDto);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    //By Admin
    @PutMapping("/doctor/editDoctor/{id}")
    public ResponseEntity<Doctor> editDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorService.editDoctor(id, doctorDto);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    //By Admin
    @GetMapping("/doctor/getDoctor/{id}")
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable Long id) {
        DoctorDto doctorDto = doctorService.getDoctor(id);
        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    // By doctor, staff, patient, and admin
    @GetMapping("/doctor/allDoctors")
    public ResponseEntity<List<DoctorDto>> allDoctors(@RequestParam(required = false) String firstname,
                                                     @RequestParam(required = false) String lastname,
                                                     @RequestParam(required = false) String specialization,
                                                     @RequestParam(required = false) Long licenceNumber) {
        List<DoctorDto> doctors = doctorService.allDoctors(firstname, lastname, specialization, licenceNumber);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    //By Admin
    @DeleteMapping("/doctor/deleteDoctor/{id}")
    public void DeleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
