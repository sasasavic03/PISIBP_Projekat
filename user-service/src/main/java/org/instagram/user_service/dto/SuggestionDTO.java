package org.instagram.user_service.dto;

public class SuggestionDTO {
    private Long id;
    private String username;
    private String profilePictureUrl;
    private Integer mutualFollowers;

    public SuggestionDTO(Long id, String username, String profilePictureUrl, Integer mutualFollowers) {
        this.id = id;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.mutualFollowers = mutualFollowers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Integer getMutualFollowers() {
        return mutualFollowers;
    }

    public void setMutualFollowers(Integer mutualFollowers) {
        this.mutualFollowers = mutualFollowers;
    }
}
