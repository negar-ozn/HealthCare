package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.MedicalRecordDto;
import com.healthcare.healthcare.entitiy.MedicalRecord;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/v1/api/")
public class MedicalRecordController {

    //All services run by Doctor!
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("medicalRecord/newMedicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@Valid @RequestBody MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordService.addMedicalRecord(medicalRecordDto);
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @PutMapping("medicalRecord/editMedicalRecord/{id}")
    public ResponseEntity<MedicalRecord> editMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = medicalRecordService.editMedicalRecord(id, medicalRecordDto);
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @GetMapping("medicalRecord/getMedicalRecord/{id}")
    public ResponseEntity<MedicalRecordDto> getMedicalRecord(@PathVariable Long id) {
        MedicalRecordDto medicalRecord = medicalRecordService.getMedicalRecord(id);
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @GetMapping("medicalRecord/allMedicalRecord")
    public ResponseEntity<List<MedicalRecordDto>> allMedicalRecords(@RequestParam(required = false) Long nationalId) {
        List<MedicalRecordDto> medicalRecord = medicalRecordService.allMedicalRecord(nationalId);
        return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
    }

    @DeleteMapping("medicalRecord/deleteMedicalRecord/{id}")
    public void deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
    }
}
