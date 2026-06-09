package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.entitiy.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {

    private Long id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate visitDate;
    private String symptoms;
    private String notes;
    private String prescriptions;
}
