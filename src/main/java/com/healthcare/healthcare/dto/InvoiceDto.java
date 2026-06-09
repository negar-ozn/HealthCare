package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Appointment;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private Long id;
    private Patient patient;
    private Appointment appointment;
    private Double amount;
    private Double insuranceCoveredAmount;
    private Double patientPayableAmount;
    private Status status;
}
