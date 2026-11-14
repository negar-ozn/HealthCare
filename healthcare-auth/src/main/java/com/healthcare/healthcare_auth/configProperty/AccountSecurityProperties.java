package com.healthcare.healthcare_auth.configProperty;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.account")
@Getter
@Setter
public class AccountSecurityProperties {

    private int maxFailedAttempts;
    private long lockDuration;
}
