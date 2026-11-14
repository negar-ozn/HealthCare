package com.healthcare.healthcare_auth.repository;

import com.healthcare.healthcare_auth.entity.RefreshToken;
import com.healthcare.healthcare_auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUser(Users user);
    void deleteByUser(Users user);
    void deleteByExpiryDateBefore(Instant now);
}
