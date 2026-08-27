package com.healthcare.healthcare_auth.forgetPassword;

import com.healthcare.healthcare_auth.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @Column(nullable = false)
    private Instant expiryDate;
    @Column(nullable = false)
    private Instant createdAt;
}
