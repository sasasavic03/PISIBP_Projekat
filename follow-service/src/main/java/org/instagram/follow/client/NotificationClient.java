package org.instagram.follow.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate;

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
                    "http://localhost:8084/api/notifications",
                    request,
                    Void.class
            );
        } catch (Exception e){
            //ako se ne posalje notifikacija ne blokiramo follow
        }
    }
}
