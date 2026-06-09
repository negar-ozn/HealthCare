package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Insurance;
import com.healthcare.healthcare.enums.Gender;
import com.healthcare.healthcare.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private Gender gender;
    private Long nationalId;
    private Long phone;
    private String address;
    private Insurance insurance;
    private Status status;
}
