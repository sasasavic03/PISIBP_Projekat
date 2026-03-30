package org.instagram.notification.repository;

import org.instagram.notification.model.Notification;
import org.instagram.notification.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
    List<Notification> findByRecipientIdAndIsReadFalse(Long recipientId);

    // dodaj ovo:
    Optional<Notification> findBySenderIdAndRecipientIdAndType(
            Long senderId, Long recipientId, NotificationType type);
}
