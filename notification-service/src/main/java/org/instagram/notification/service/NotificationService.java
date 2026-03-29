package org.instagram.notification.service;

import org.instagram.notification.client.FollowServiceClient;
import org.instagram.notification.client.UserServiceClient;
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
    private final UserServiceClient userServiceClient;
    private final FollowServiceClient followServiceClient;

    public NotificationService(NotificationRepository notificationRepository,
                               UserServiceClient userServiceClient,
                               FollowServiceClient followServiceClient) {
        this.notificationRepository = notificationRepository;
        this.userServiceClient = userServiceClient;
        this.followServiceClient = followServiceClient;
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
                .map(n -> {
                    NotificationResponseDto dto = new NotificationResponseDto(
                            n.getId(),
                            n.getType(),
                            n.getSenderId(),
                            n.getIsRead(),
                            n.getCreatedAt()
                    );
                    UserServiceClient.UserResponse user = userServiceClient.getUserById(n.getSenderId());
                    if (user != null) {
                        dto.setUsername(user.getUsername());
                        dto.setAvatar(user.getAvatar());
                    }
                    return dto;
                })
                .toList();
    }

    public List<NotificationResponseDto> getUnreadNotifications(Long recipientId){
        return notificationRepository.findByRecipientIdAndIsReadFalse(recipientId)
                .stream()
                .map(n -> new NotificationResponseDto(
                        n.getId(),
                        n.getType(),
                        n.getSenderId(),
                        n.getIsRead(),
                        n.getCreatedAt()
                ))
                .toList();
    }
    public void markAsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    public void acceptFollowRequest(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        followServiceClient.acceptFollow(notification.getSenderId(), notification.getRecipientId());

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void declineFollowRequest(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }

}
