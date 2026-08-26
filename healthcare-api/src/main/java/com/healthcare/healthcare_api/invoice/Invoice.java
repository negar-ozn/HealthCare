package com.healthcare.healthcare_api.invoice;

import com.healthcare.healthcare_api.appointment.Appointment;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private BigDecimal tax;
    @Column(nullable = false)
    private BigDecimal insuranceCoveredAmount;
    @Column(nullable = false)
    private BigDecimal patientPayableAmount;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false, unique = true)
    private String invoiceNumber;
    @Version
    private Long version;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}
