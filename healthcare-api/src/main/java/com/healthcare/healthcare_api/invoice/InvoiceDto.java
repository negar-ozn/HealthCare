package com.healthcare.healthcare_api.invoice;

import com.healthcare.healthcare_api.appointment.Appointment;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private Long id;
    private Patient patient;
    private Appointment appointment;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal insuranceCoveredAmount;
    private BigDecimal patientPayableAmount;
    private Status status;
    private String invoiceNumber;
    private Instant createdAt;
    private Instant updatedAt;
}
