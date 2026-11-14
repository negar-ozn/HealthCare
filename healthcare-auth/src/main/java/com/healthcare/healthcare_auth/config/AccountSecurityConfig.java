package com.healthcare.healthcare_auth.config;

import com.healthcare.healthcare_auth.configProperty.AccountSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccountSecurityProperties.class)
public class AccountSecurityConfig {
}
