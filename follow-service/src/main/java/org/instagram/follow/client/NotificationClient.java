package org.instagram.follow.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${services.notification.url:http://notification-service:8080}")
    private String notificationServiceUrl;

    public NotificationClient(){
        this.restTemplate = new RestTemplate();
    }

    public void sendNotification(Long recipientId, Long senderId, String type){
        try{
            Map<String, Object> request = Map.of(
                    "recipientId", recipientId,
                    "senderId", senderId,
                    "type", type
            );
            restTemplate.postForObject(
                    notificationServiceUrl + "/api/notification",
                    request,
                    Void.class
            );
            System.out.println("Notification sent successfully to " + notificationServiceUrl);
        } catch (Exception e){
            System.out.println("Failed to send notification: " + e.getMessage());
        }
    }
}
