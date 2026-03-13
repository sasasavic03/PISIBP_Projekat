package org.instagram.notification.dto;

import org.instagram.notification.model.NotificationType;

public class NotificationRequestDto {

    private Long recipientId;
    private Long senderId;
    private NotificationType type;

    public Long getRecipientId() {return recipientId;}
    public void setRecipientId(Long recipientId) {this.recipientId = recipientId;}

    public Long getSenderId() {return senderId;}
    public void setSenderId(Long senderId) {this.senderId = senderId;}

    public NotificationType getType() {return type;}
    public void setType(NotificationType type) {this.type = type;}
}
