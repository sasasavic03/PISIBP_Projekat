package org.instagram.user_service.dto;

public class UserSearchResultDTO {
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private boolean isPrivate;

    public UserSearchResultDTO(Long id, String username, String email, 
                              String profilePictureUrl, boolean isPrivate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.isPrivate = isPrivate;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
