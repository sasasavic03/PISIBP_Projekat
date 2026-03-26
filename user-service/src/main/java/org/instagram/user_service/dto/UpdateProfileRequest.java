package org.instagram.user_service.dto;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    
    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;

    private Boolean isPrivate;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
