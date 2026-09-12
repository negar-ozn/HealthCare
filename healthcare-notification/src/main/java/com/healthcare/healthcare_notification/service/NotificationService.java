package com.healthcare.healthcare_notification.service;

import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.healthcare_notification.entity.Notification;
import com.healthcare.healthcare_notification.enums.NotificationSource;
import com.healthcare.healthcare_notification.enums.NotificationStatus;
import com.healthcare.healthcare_notification.enums.NotificationType;
import com.healthcare.healthcare_notification.repository.NotificationRepository;
import com.healthcare.healthcare_notification.template.EmailTemplateService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@Slf4j
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

    public void processEmailNotification(NotificationEvent event) {

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

            commonKafkaProducer.publishEmailSentEvent(CoreEvent.builder()
                    .email(event.recipient())
                    .action(NotificationType.EMAIL_SENT.name())
                    .username((String) event.variables().get("username"))
                    .entityName(EntityName.NOTIFICATION)
                    .status(NotificationStatus.SUCCESSFUL.name())
                    .details("Verification email sent successfully")
                    .eventType(EventType.EMAIL_SENT)
                    .userId(event.userId())
                    .build());


        } catch (Exception ex) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(ex.getMessage());

            commonKafkaProducer.publishEmailSentEvent(CoreEvent.builder()
                    .email(event.recipient())
                    .action(NotificationType.EMAIL_SENT.name())
                    .username((String) event.variables().get("username"))
                    .entityName(EntityName.NOTIFICATION)
                    .status(NotificationStatus.FAILED.name())
                    .details("Failed to send verification email")
                    .eventType(EventType.EMAIL_SENT)
                    .userId(event.userId())
                    .build());
        }
        repository.save(notification);
    }

    public void processWelcomeEmailNotification(NotificationEvent event) {

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

            commonKafkaProducer.publishEmailSentEvent(CoreEvent.builder()
                    .email(event.recipient())
                    .action(NotificationType.WELCOME_EMAIL_SENT.name())
                    .username((String) event.variables().get("username"))
                    .entityName(EntityName.NOTIFICATION)
                    .status(NotificationStatus.SUCCESSFUL.name())
                    .details("Welcome email sent successfully")
                    .eventType(EventType.WELCOME_EMAIL)
                    .userId(event.userId())
                    .build());

        } catch (Exception ex) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(ex.getMessage());

            commonKafkaProducer.publishEmailSentEvent(CoreEvent.builder()
                    .email(event.recipient())
                    .action(NotificationType.WELCOME_EMAIL_SENT.name())
                    .username((String) event.variables().get("username"))
                    .entityName(EntityName.NOTIFICATION)
                    .status(NotificationStatus.FAILED.name())
                    .details("Failed to send welcome email")
                    .eventType(EventType.WELCOME_EMAIL)
                    .userId(event.userId())
                    .build());
        }
        repository.save(notification);
    }

    public void processForgetPasswordEmailNotification(NotificationEvent event) {

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

            commonKafkaProducer.publishForgetPasswordEmailSentEvent(CoreEvent.builder()
                    .email(event.recipient())
                    .action(NotificationType.FORGET_PASSWORD_EMAIL.name())
                    .username((String) event.variables().get("username"))
                    .entityName(EntityName.NOTIFICATION)
                    .status(NotificationStatus.SUCCESSFUL.name())
                    .details("Forget password email sent successfully")
                    .eventType(EventType.FORGET_PASSWORD_EMAIL)
                    .userId(event.userId())
                    .build());

        } catch (Exception ex) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(ex.getMessage());

            commonKafkaProducer.publishForgetPasswordEmailSentEvent(CoreEvent.builder()
                    .email(event.recipient())
                    .action(NotificationType.FORGET_PASSWORD_EMAIL.name())
                    .username((String) event.variables().get("username"))
                    .entityName(EntityName.NOTIFICATION)
                    .status(NotificationStatus.FAILED.name())
                    .details("Failed to send password email")
                    .eventType(EventType.FORGET_PASSWORD_EMAIL)
                    .userId(event.userId())
                    .build());
        }
        repository.save(notification);
    }
}
