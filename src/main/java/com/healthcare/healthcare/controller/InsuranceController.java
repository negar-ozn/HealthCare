package com.healthcare.healthcare.controller;

import com.healthcare.healthcare.dto.InsuranceDto;
import com.healthcare.healthcare.service.InsuranceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/v1/api/Insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @PostMapping("/newInsurance")
    public ResponseEntity<InsuranceDto> addInsurance(@RequestBody InsuranceDto insuranceDto) {
        InsuranceDto insurance = insuranceService.addInsurance(insuranceDto);
        return new ResponseEntity<>(insurance, HttpStatus.OK);
    }

    @PutMapping("/editInsurance/{id}")
    public ResponseEntity<InsuranceDto> editInsurance(@PathVariable Long id) {
        InsuranceDto insurance = insuranceService.editInsurance(id);
        return new ResponseEntity<>(insurance, HttpStatus.OK);
    }

    @GetMapping("/getInsurance/{id}")
    public ResponseEntity<InsuranceDto> getInsurance(@PathVariable Long id) {
        InsuranceDto insurance = insuranceService.getInsurance(id);
        return new ResponseEntity<>(insurance, HttpStatus.OK);
    }

    @GetMapping("/allInsurance")
    public ResponseEntity<InsuranceDto> allInsurance(@RequestParam(required = false) Double coveragePercentage,
                                                     @RequestParam(required = false) Long policyNumber) {
        InsuranceDto insurance = insuranceService.allInsurance(coveragePercentage, policyNumber);
        return new ResponseEntity<>(insurance, HttpStatus.OK);
    }

    @DeleteMapping("/deleteInsurance/{id}")
    public void deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
    }
}
