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
                    notificationServiceUrl + "/api/notifications",
                    request,
                    Void.class
            );
        } catch (Exception e){
            //ako se ne posalje notifikacija ne blokiramo follow
        }
    }
}
