package org.instagram.notification.dto;

import org.instagram.notification.model.NotificationType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.apache.catalina.manager.StatusTransformer.formatTime;

public class NotificationResponseDto {

    private Long id;
    private String type;
    private Long senderId;
    private String username;
    private String avatar;
    private String message;
    private String time;
    private boolean read;

    public NotificationResponseDto(Long id, NotificationType type, Long senderid, boolean isRead, LocalDateTime createdAt ) {
        this.id = id;
        this.type = type.name().toLowerCase();
        this.senderId = senderid;
        this.read = isRead;
        this.time = formatTime(createdAt);
        this.message = getMessageForType(type);
    }

    private String formatTime(LocalDateTime createdAt) {
        if(createdAt == null) return "";
        long minutes = ChronoUnit.MINUTES.between(createdAt,LocalDateTime.now());
        if(minutes <60) return minutes + "m";
        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        if(hours<24) return hours + "h";
        long days = ChronoUnit.DAYS.between(createdAt, LocalDateTime.now());
        return days + "d";
    }

    private String getMessageForType(NotificationType type) {
        return switch(type){
            case FOLLOW_REQUEST -> "Wants to follows you.";
            case FOLLOW_ACCEPTED -> "Accepted your follow request";
            case NEW_FOLLOWER -> "Started following you";
        };
    }

    public Long getId(){return id;}
    public String getType(){return type;}
    public Long getSenderId(){return senderId;}
    public String getUsername(){return username;}
    public void setUername(String username){this.username = username;}
    public String getAvatar(){return avatar;}
    public void setAvatar(String avatar){this.avatar = avatar;}
    public String getMessage(){return message;}
    public String getTime(){ return message;}
    public boolean isRead(){return read;}



}
