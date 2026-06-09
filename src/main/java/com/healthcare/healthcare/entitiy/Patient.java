package com.healthcare.healthcare.entitiy;

import com.healthcare.healthcare.enums.Gender;
import com.healthcare.healthcare.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "PATIENT")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private Gender gender;
    private Long nationalId;
    private Long phone;
    private String address;
    private Boolean arrival;
    @OneToOne
    @JoinColumn(name = "INSURANCE_ID")
    private Insurance insurance;
    private Status status;
    @OneToOne
    private Users userInfo;
}
