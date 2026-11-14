package com.healthcare.healthcare_auth.repository;

import com.healthcare.healthcare_auth.entity.PasswordSetupToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordSetupTokenRepository extends JpaRepository<PasswordSetupToken, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT t
            FROM PasswordSetupToken t
            WHERE t.tokenHash = :tokenHash
            """)
    Optional<PasswordSetupToken> findByTokenHashForUpdate(@Param("tokenHash") String tokenHash);

    @Modifying
    @Query("""
    update PasswordSetupToken t
    set t.used = true
    where t.user.id = :userId
      and t.used = false
    """)
    void invalidateUnusedTokensByUserId(@Param("userId") Long userId);
}
