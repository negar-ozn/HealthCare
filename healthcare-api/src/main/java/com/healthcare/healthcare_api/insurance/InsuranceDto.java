package com.healthcare.healthcare_api.insurance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDto {

    private Long id;
    private String providerName;
    private Double coveragePercentage;
    private Long policyNumber;
    private LocalDate expiryDate;
}
