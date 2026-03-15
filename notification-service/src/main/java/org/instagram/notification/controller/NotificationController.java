package org.instagram.notification.controller;


import org.instagram.notification.dto.NotificationRequestDto;
import org.instagram.notification.dto.NotificationResponseDto;
import org.instagram.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@PathVariable Long userId){
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @PutMapping("/{userId}/read-all")
    public ResponseEntity<Map<String,String>> markAllAsRead(@PathVariable Long userId){
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    @PostMapping("/follow-request/{notificationId}/accept")
    public ResponseEntity<Map<String, String>> acceptFollowRequest(@PathVariable Long notificationId){
        notificationService.acceptFollowRequest(notificationId);
        return ResponseEntity.ok(Map.of("message", "Follow request accepted"));
    }

    @DeleteMapping("/follow-request/{notificationId}/decline")
    public ResponseEntity<Map<String, String>> declineFollowRequest(@PathVariable Long notificationId){
        notificationService.declineFollowRequest(notificationId);
        return ResponseEntity.ok(Map.of("message", "Follow request declined"));
    }

}
