package org.instagram.notification.dto;

import org.instagram.notification.model.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponseDto {

    private Long id;
    private Long recipientId;
    private Long senderId;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponseDto(Long id, Long recipientId, Long senderId, NotificationType type, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {return id;}
    public Long getRecipientId() {return recipientId;}
    public Long getSenderId() {return senderId;}
    public NotificationType getType() {return type;}
    public boolean isRead() {return isRead;}
    public LocalDateTime getCreatedAt() {return createdAt;}
}
