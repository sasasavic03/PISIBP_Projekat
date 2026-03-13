package org.instagram.notification.controller;


import org.instagram.notification.dto.NotificationRequestDto;
import org.instagram.notification.dto.NotificationResponseDto;
import org.instagram.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> createNotification(@RequestBody NotificationRequestDto request){
        notificationService.createNotification(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{recipientId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@PathVariable Long recipientId){
        return ResponseEntity.ok(notificationService.getNotifications(recipientId));
    }

    @GetMapping("/{recipientId}/unread")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadNotifications(@PathVariable Long recipientId){
        return ResponseEntity.ok(notificationService.getUnreadNotifications(recipientId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markasRead(@PathVariable Long notificationId){
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

}
