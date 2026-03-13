package org.instagram.notification.service;

import org.instagram.notification.dto.NotificationRequestDto;
import org.instagram.notification.dto.NotificationResponseDto;
import org.instagram.notification.model.Notification;
import org.instagram.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(NotificationRequestDto request){
        Notification notification = new Notification();
        notification.setRecipientId(request.getRecipientId());
        notification.setSenderId(request.getSenderId());
        notification.setType(request.getType());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDto> getNotifications(Long recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId)
                .stream()
                .map(n -> new NotificationResponseDto(
                        n.getId(),
                        n.getRecipientId(),
                        n.getSenderId(),
                        n.getType(),
                        n.getIsRead(),
                        n.getCreatedAt()
                ))
                .toList();
    }

    public List<NotificationResponseDto> getUnreadNotifications(Long recipientId){
        return notificationRepository.findByRecipientIdAndIsReadFalse(recipientId)
                .stream()
                .map(n -> new NotificationResponseDto(
                        n.getId(),
                        n.getRecipientId(),
                        n.getSenderId(),
                        n.getType(),
                        n.getIsRead(),
                        n.getCreatedAt()
                ))
                .toList();
    }

    public void markAsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->new  RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

}
