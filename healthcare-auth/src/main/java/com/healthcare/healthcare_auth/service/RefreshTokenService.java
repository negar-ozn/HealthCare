package com.healthcare.healthcare_auth.service;

import com.healthcare.healthcare_auth.entity.RefreshToken;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.exception.RefreshTokenExpiredException;
import com.healthcare.healthcare_auth.exception.RefreshTokenRevokedException;
import com.healthcare.healthcare_auth.exception.TokenNotFoundException;
import com.healthcare.healthcare_auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(Users user) {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(UUID.randomUUID().toString())
                        .user(user)
                        .expiryDate(Instant.now().plusMillis(refreshExpiration))
                        .revoked(false)
                        .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if(token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException("Refresh token expired");
        }
        return token;
    }

    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = findByToken(token);
        if(refreshToken.isRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token revoked");
        }
        return verifyExpiration(refreshToken);
    }

    public void revoke(RefreshToken token) {

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    //usage: Patient changes their password.
    public void revokeAllUserTokens(Users user) {

        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);
        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }

    public void delete(RefreshToken refreshToken) {

        refreshTokenRepository.delete(refreshToken);

    }

    @Scheduled(cron = "0 0 2 * * *")
    public void deleteExpiredTokens() {

        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
