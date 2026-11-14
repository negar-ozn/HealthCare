package com.healthcare.healthcare_notification.template;

import com.healthcare.common_kafka.event.NotificationEvent;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(NotificationEvent event) {

        Context context = new Context();
        event.variables().forEach(context::setVariable);
        String template = switch (event.type()) {
            case WELCOME -> "welcome";
            case PASSWORD_RESET -> "password-reset";
            case NEW_PASSWORD -> "new-password";
            case EMAIL_VERIFICATION -> "email-verification";
            case APPOINTMENT_CREATED -> "appointment-created";
            case PERSONNEL_PASSWORD_SETUP_LINK -> "personnel-password-setup-link";
            case PERSONNEL_PASSWORD_SETUP -> "password-setup";
            default -> throw new IllegalArgumentException("Unsupported notification type.");
        };
        return templateEngine.process(template, context);
    }
}