package com.healthcare.healthcare_api.patient;

import com.healthcare.healthcare_api.shared.enums.Gender;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.insurance.Insurance;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, unique = true, length = 10)
    private Long nationalId;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    private Boolean arrival;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, unique = true)
    private String patientNumber;

    @Version
    private Long version;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;
}
