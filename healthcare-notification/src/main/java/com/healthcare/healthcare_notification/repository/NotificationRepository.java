package com.healthcare.healthcare_notification.repository;

import com.healthcare.healthcare_notification.entity.Notification;
import com.healthcare.healthcare_notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByStatus(NotificationStatus status);
}
