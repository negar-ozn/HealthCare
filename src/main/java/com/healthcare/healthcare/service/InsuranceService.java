package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.InsuranceDto;
import com.healthcare.healthcare.repository.InsuranceRepository;
import org.springframework.stereotype.Service;

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

    public InsuranceDto allInsurance(Double coveragePercentage, Long policyNumber) {
        return null;
    }

    public void deleteInsurance(Long id) {

    }
}
