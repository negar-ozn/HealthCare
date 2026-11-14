package com.healthcare.healthcare_auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true, length = 50)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique = true, length = 100)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private boolean isEnabled = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountLocked = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer failedLoginAttempts = 0;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant lockTime;

    @Version
    private Long version;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public void addRefreshToken(RefreshToken token) {
        refreshTokens.add(token);
        token.setUser(this);
    }

    public void removeRefreshToken(RefreshToken token) {
        refreshTokens.remove(token);
        token.setUser(null);
    }
}
