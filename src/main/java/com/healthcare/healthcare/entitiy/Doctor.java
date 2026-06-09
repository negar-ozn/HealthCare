package com.healthcare.healthcare.entitiy;

import com.healthcare.healthcare.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "DOCTOR")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private Long licenceNumber;
    private Boolean availabilitySchedule;
    private Status status;
    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Users userInfo;
}
