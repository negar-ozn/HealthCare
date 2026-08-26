package com.healthcare.healthcare_api.insurance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api/v1/Insurance"))
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @PostMapping("/newInsurance")
    public ResponseEntity<InsuranceDto> addInsurance(@RequestBody InsuranceDto insuranceDto) {
        InsuranceDto insurance = insuranceService.addInsurance(insuranceDto);
        return ResponseEntity.ok(insurance);
    }

    @PutMapping("/editInsurance/{id}")
    public ResponseEntity<InsuranceDto> editInsurance(@PathVariable Long id) {
        InsuranceDto insurance = insuranceService.editInsurance(id);
        return ResponseEntity.ok(insurance);
    }

    @GetMapping("/getInsurance/{id}")
    public ResponseEntity<InsuranceDto> getInsurance(@PathVariable Long id) {
        InsuranceDto insurance = insuranceService.getInsurance(id);
        return ResponseEntity.ok(insurance);
    }

    @GetMapping("/allInsurance")
    public ResponseEntity<List<InsuranceDto>> allInsurance(@RequestParam(required = false) Double coveragePercentage,
                                                           @RequestParam(required = false) Long policyNumber) {
        List<InsuranceDto> insurance = insuranceService.allInsurance(coveragePercentage, policyNumber);
        return ResponseEntity.ok(insurance);
    }

    @DeleteMapping("/deleteInsurance/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }
}
