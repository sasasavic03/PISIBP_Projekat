package org.instagram.notification.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="notification_db")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_id",nullable = false)
    private Long RecipientId;

    @Column(name = "sender_id",nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getRecipientId() {return RecipientId;}
    public void setRecipientId(Long RecipientId) {this.RecipientId = RecipientId;}

    public Long getSenderId() {return senderId;}
    public void setSenderId(Long senderId) {this.senderId = senderId;}

    public NotificationType getType() {return type;}
    public void setType(NotificationType type) {this.type = type;}

    public boolean getIsRead() { return isRead; }
    public void setRead(Boolean isRead) {this.isRead = isRead;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
