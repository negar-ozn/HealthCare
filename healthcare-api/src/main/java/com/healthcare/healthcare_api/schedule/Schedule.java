package com.healthcare.healthcare_api.schedule;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.enums.Weekday;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Weekday weekday;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; //Reserved, Available, Canceled
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    @Version
    private Long version;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

}
