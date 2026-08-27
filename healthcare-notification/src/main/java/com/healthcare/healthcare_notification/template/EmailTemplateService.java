package com.healthcare.healthcare_notification.template;

import com.healthcare.healthcare_notification.kafka.event.NotificationEvent;
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
            case EMAIL_VERIFICATION -> "email-verification";
            case APPOINTMENT_CREATED -> "appointment-created";
            default -> throw new IllegalArgumentException("Unsupported notification type.");
        };
        return templateEngine.process(
                template,
                context);
    }
}


//switch (event.type()) {
//        case WELCOME -> renderWelcome(event.variables());
//        case PASSWORD_RESET -> renderPasswordReset(event.variables());
//        case APPOINTMENT_CREATED -> renderAppointmentCreated(event.variables());
//        }