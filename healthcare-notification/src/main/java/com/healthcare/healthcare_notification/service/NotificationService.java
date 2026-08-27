package com.healthcare.healthcare_notification.service;

import com.healthcare.healthcare_notification.dto.VerificationEmailRequest;
import com.healthcare.healthcare_notification.entity.Notification;
import com.healthcare.healthcare_notification.enums.EntityName;
import com.healthcare.healthcare_notification.enums.NotificationStatus;
import com.healthcare.healthcare_notification.enums.NotificationType;
import com.healthcare.healthcare_notification.kafka.event.EmailSentEvent;
import com.healthcare.healthcare_notification.kafka.event.NotificationEvent;
import com.healthcare.healthcare_notification.kafka.producer.EmailSentProducer;
import com.healthcare.healthcare_notification.repository.NotificationRepository;
import com.healthcare.healthcare_notification.template.EmailTemplateService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository repository;
    private final EmailTemplateService templateService;
    private final EmailService emailService;
    private final EmailSentProducer emailSentProducer;

    public NotificationService(NotificationRepository repository, EmailTemplateService templateService, EmailService emailService, EmailSentProducer emailSentProducer) {
        this.repository = repository;
        this.templateService = templateService;
        this.emailService = emailService;
        this.emailSentProducer = emailSentProducer;
    }

    public void processNotification(NotificationEvent event) {

        Notification notification = Notification.builder()
                .correlationId(event.correlationId())
                .recipient(event.recipient())
                .subject(event.subject())
                .notificationSource(event.sender())
                .type(event.type())
                .status(NotificationStatus.PENDING)
                .build();
        repository.save(notification);
        try {
            String html = templateService.render(event);
            notification.setBody(html);
            emailService.send(notification);
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(Instant.now());
        } catch (Exception ex) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(ex.getMessage());
        }
        repository.save(notification);
    }

    public void sendVerificationEmail(VerificationEmailRequest request) {

        String subject = "Verify your email";
        String body = """
                Hello %s,

                Welcome to our application!

                Please verify your email by clicking
                the link below:

                %s

                """.formatted(
                request.username(),
                request.verificationLink()
        );
        emailService.sendEmail(request.email(), subject, body);
        emailSentProducer.publishEmailSentEvent(EmailSentEvent.builder()
                .userId(request.userId())
                .email(request.email())
                .username(request.email())
                .auditAction(NotificationType.EMAIL_SENT.name())
                .entityName(EntityName.NOTIFICATION.name())
                .auditStatus(NotificationStatus.SENT.name())
                .details("Verification email sent successfully")
                .build());
    }
}
