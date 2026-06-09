package com.healthcare.healthcare.entitiy;

import com.healthcare.healthcare.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "INVOICE")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Double insuranceCoveredAmount;
    private Double patientPayableAmount;
    private Status status;
    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;
    @OneToOne
    @JoinColumn(name = "APPOINTMENT_ID")
    private Appointment appointment;
}
