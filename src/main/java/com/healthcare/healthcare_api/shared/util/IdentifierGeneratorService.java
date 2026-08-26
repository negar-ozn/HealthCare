package com.healthcare.healthcare_api.shared.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class IdentifierGeneratorService {

    private final StringRedisTemplate stringRedisTemplate;

    public IdentifierGeneratorService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String generatorAppointmentIdentifier() {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        Long sequence = stringRedisTemplate.opsForValue().increment("appointment:" + date);
        return "APT".concat(date).concat(String.format("%06d", sequence));
    }

    public String generatePatientIdentifier() {

        Long sequence = stringRedisTemplate.opsForValue().increment("patient_counter");
        return "PAT".concat(String.format("%06d", sequence));
    }

    public String generatePrescriptionIdentifier() {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        Long sequence = stringRedisTemplate.opsForValue().increment("prescription:" + date);
        return "PRS".concat(date).concat(String.format("%06d", sequence));
    }

    public String generatePersonnelIdentifier() {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        Long sequence = stringRedisTemplate.opsForValue().increment("personnel:" + date);
        return "PRSNL".concat(date).concat(String.format("%06d", sequence));
    }

    public String generateInvoiceIdentifier() {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        Long sequence = stringRedisTemplate.opsForValue().increment("invoice:" + date);
        return "INV".concat(date).concat(String.format("%06d", sequence));
    }
}
