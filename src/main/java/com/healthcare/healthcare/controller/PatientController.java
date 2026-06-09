package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.PatientDto;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/patients/newPatient")
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient patient = patientService.addPatient(patientDto);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @PutMapping("/patients/editPatient/{id}")
    public ResponseEntity<Patient> editPatient(@PathVariable Long id, @Valid @RequestBody PatientDto patientDto) {
        Patient patient = patientService.editPatient(id, patientDto);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping("/patients/getPatient/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id) {
        PatientDto patientDto = patientService.getPatient(id);
        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

    @GetMapping("/patients/allPatients")
    public ResponseEntity<List<PatientDto>> allPatient(@RequestParam(required = false) String firstName,
                                                       @RequestParam(required = false) String lastName,
                                                       @RequestParam(required = false) Long nationalId) {
        List<PatientDto> patientList = patientService.allPatient(firstName, lastName, nationalId);
        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

    @DeleteMapping("/patients/deletePatient/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @GetMapping("/patients/history")
    public ResponseEntity<List<PatientDto>> patientHistory(@RequestParam(required = false) Long nationalId,
                                                           @RequestParam(required = false) Long id) {
        List<PatientDto> patientDtoList = patientService.patientHistory(id, nationalId);
        return new ResponseEntity<>(patientDtoList, HttpStatus.OK);
    }
}
