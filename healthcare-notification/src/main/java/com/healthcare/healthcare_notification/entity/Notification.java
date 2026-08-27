package com.healthcare.healthcare_notification.entity;

import com.healthcare.healthcare_notification.enums.NotificationSource;
import com.healthcare.healthcare_notification.enums.NotificationStatus;
import com.healthcare.healthcare_notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @Column(nullable = false)
    private String recipient;
    @Column(nullable = false)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    @Enumerated(EnumType.STRING)
    private NotificationSource notificationSource;
    @Column(nullable = false)
    private String correlationId;
    @CreationTimestamp
    private Instant createdAt;
    private Instant sentAt;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
