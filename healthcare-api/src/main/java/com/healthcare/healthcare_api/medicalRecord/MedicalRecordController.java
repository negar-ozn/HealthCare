package com.healthcare.healthcare_api.medicalRecord;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/medicalRecords")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PreAuthorize("hasAuthority('DOCTOR_CREATE')")
    @PostMapping("medicalRecord/newMedicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@Valid @RequestBody MedicalRecordRequest medicalRecordRequest) {
        MedicalRecord medicalRecord = medicalRecordService.addMedicalRecord(medicalRecordRequest);
        return ResponseEntity.ok(medicalRecord);
    }

    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    @PutMapping("medicalRecord/editMedicalRecord/{id}")
    public ResponseEntity<MedicalRecord> editMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequest medicalRecordRequest) {
        MedicalRecord medicalRecord = medicalRecordService.editMedicalRecord(id, medicalRecordRequest);
        return ResponseEntity.ok(medicalRecord);
    }

    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @GetMapping("medicalRecord/getMedicalRecord/{id}")
    public ResponseEntity<MedicalRecordResponse> getMedicalRecord(@PathVariable Long id) {
        MedicalRecordResponse medicalRecord = medicalRecordService.getMedicalRecord(id);
        return ResponseEntity.ok(medicalRecord);
    }

    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    @GetMapping("medicalRecord/allMedicalRecord")
    public ResponseEntity<List<MedicalRecordResponse>> allMedicalRecords(@RequestParam(required = false) Long nationalId) {
        List<MedicalRecordResponse> medicalRecord = medicalRecordService.allMedicalRecord(nationalId);
        return ResponseEntity.ok(medicalRecord);
    }

    @PreAuthorize("hasAuthority('DOCTOR_DELETE')")
    @DeleteMapping("medicalRecord/deleteMedicalRecord/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
