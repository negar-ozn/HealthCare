package com.healthcare.healthcare_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
class HealthCareApplication {

    static void main(String[] args) {
        SpringApplication.run(HealthCareApplication.class, args);
    }

}
