package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.enums.AppointmentStatus;
import com.healthcare.healthcare.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private String visitReason;
    private String cancellationReason;
    private Boolean rescheduled;
    private Patient patient;
    private Doctor doctor;
}
