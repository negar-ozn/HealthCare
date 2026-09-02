package com.healthcare.healthcare_notification.service;

import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.common_kafka.event.EmailSentEvent;
import com.healthcare.healthcare_notification.dto.VerificationEmailRequest;
import com.healthcare.healthcare_notification.entity.Notification;
import com.healthcare.healthcare_notification.enums.EntityName;
import com.healthcare.healthcare_notification.enums.NotificationSource;
import com.healthcare.healthcare_notification.enums.NotificationStatus;
import com.healthcare.healthcare_notification.enums.NotificationType;
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
    private final CommonKafkaProducer commonKafkaProducer;

    public NotificationService(NotificationRepository repository, EmailTemplateService templateService,
                               EmailService emailService, CommonKafkaProducer commonKafkaProducer) {
        this.repository = repository;
        this.templateService = templateService;
        this.emailService = emailService;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    public void processNotification(NotificationEvent event) {

        Notification notification = Notification.builder()
                .correlationId(event.correlationId())
                .recipient(event.recipient())
                .subject(event.subject())
                .notificationSource(NotificationSource.valueOf(event.sender().toString()))
                .type(NotificationType.valueOf(event.type().toString()))
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
        commonKafkaProducer.publishEmailSentEvent(EmailSentEvent.builder()
                .userId(request.userId())
                .email(request.email())
                .username(request.email())
                .action(NotificationType.EMAIL_SENT.name())
                .entityName(EntityName.NOTIFICATION.name())
                .status(NotificationStatus.SENT.name())
                .details("Verification email sent successfully")
                .build());
    }
}
