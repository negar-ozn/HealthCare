package com.healthcare.healthcare_api.medicalRecord;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medical_records")
@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate visitDate;
    @Column(nullable = false)
    private String symptoms;
    @Column(nullable = false)
    private String notes;
    @Column(nullable = false)
    private String prescriptions;
    @Column(nullable = false, unique = true)
    private String prescriptionsNumber;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    @Version
    private Long version;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
