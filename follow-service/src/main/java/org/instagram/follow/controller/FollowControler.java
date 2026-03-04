package org.instagram.follow.controller;


import org.instagram.follow.service.FollowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService service;

    public FollowController(FollowService service){
        this.service= service;
    }

    @PostMapping("/{followingId}")
    public void follow(@PathVariable Long followingID,
                       @RequestParam Long followerId,
                       @RequestParam boolean isPrivate){
        service.follow(followerId,followingID,isPrivate);
    }
    @DeleteMapping("/{followingId}")
    public void unfollow(@PathVariable Long followingId,
                         @RequestParam Long followerId){
        service.unfollow(followerId,followingId);
    }
}
