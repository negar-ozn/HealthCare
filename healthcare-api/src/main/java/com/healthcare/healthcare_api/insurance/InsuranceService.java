package com.healthcare.healthcare_api.insurance;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;

    public InsuranceService(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public InsuranceDto addInsurance(InsuranceDto insuranceDto) {
        return null;
    }

    public InsuranceDto editInsurance(Long id) {
        return null;
    }

    public InsuranceDto getInsurance(Long id) {
        return null;
    }

    public List<InsuranceDto> allInsurance(Double coveragePercentage, Long policyNumber) {
        return null;
    }

    public void deleteInsurance(Long id) {

    }
}
