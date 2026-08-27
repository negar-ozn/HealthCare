package com.healthcare.healthcare_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HealthcareAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareAuthApplication.class, args);
    }

}
