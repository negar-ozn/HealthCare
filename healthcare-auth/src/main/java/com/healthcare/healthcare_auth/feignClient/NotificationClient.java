package com.healthcare.healthcare_auth.feignClient;

import com.healthcare.healthcare_auth.dto.VerificationEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "healthcare-notification", url = "${notification-service.url}")
public interface NotificationClient {

    @PostMapping("/verification-email")
    void sendVerificationEmail(VerificationEmailRequest request);
}
