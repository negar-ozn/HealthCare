package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Department;
import com.healthcare.healthcare.entitiy.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Role role;
    private Department department;
}
