package com.healthcare.healthcare_audit.repository;

import com.healthcare.healthcare_audit.entity.AuditLog;
import com.healthcare.healthcare_audit.enums.AuditAction;
import com.healthcare.healthcare_audit.enums.AuditStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByTimestampBetween(Instant timestampAfter, Instant timestampBefore, Pageable pageable);
    @Query(value = "SELECT A FROM AuditLog A WHERE A.userId=:userId OR A.action=:action OR A.status=:status OR A.entityName=:entityName OR A.entityId=:entityId")
    Page<AuditLog> findByUserIdOrActionOrStatusOrEntityNameOrEntityId(Long userId, AuditAction action, AuditStatus status,
                                                                      String entityName, Long entityId, Pageable pageable);
}
