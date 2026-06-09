package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {

    private Long id;
    private Doctor doctor;
    private String day;
    private LocalDate date;
    private LocalTime time;
    private Status status; //Reserved, Available, Canceled
}
