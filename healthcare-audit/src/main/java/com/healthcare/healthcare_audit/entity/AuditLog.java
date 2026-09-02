package com.healthcare.healthcare_audit.entity;

import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;
    @Column(length = 100)
    private String entityName;
    @Column(length = 50)
    private String email;
    private Long entityId;
    @Column(nullable = false)
    private Instant timestamp;
    @Column(length = 45)
    private String ipAddress;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AuditStatus status;
    @Column(columnDefinition = "TEXT")
    private String details;
    @Column(length = 500, unique = true)
    private String verificationLink;
    @Column(length = 100)
    private String eventType;
    @Version
    private Long version;
}

