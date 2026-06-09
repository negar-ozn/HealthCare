package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Department;
import com.healthcare.healthcare.entitiy.Users;
import com.healthcare.healthcare.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private Department department;
    private Long licenceNumber;
    private Boolean availabilitySchedule;
    private Status status;
    private Users userInfo;
}
