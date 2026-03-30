package org.instagram.block.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FollowServiceClient {

    private final RestTemplate restTemplate;

    @Value("${follow.service.url:http://follow-service:8080}")
    private String followServiceUrl;

    public FollowServiceClient(){
        this.restTemplate = new RestTemplate();
    }

    public void removeFollowRelations(Long userA, Long userB){
        try{
            restTemplate.delete(followServiceUrl + "/api/follow/" + userB + "/remove?followerId=" + userA);
        } catch(Exception e){
            System.out.println("Could not remove follow A -> B " + e.getMessage());
        }
        try {
            // obrisi userB -> userA
            restTemplate.delete(followServiceUrl + "/api/follow/" + userA + "/remove?followerId=" + userB);
        } catch (Exception e) {
            System.out.println("Could not remove follow B->A: " + e.getMessage());
        }
    }
}