package org.instagram.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    
    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;

    private Boolean isPrivate;

    private String fullname;

    @Email(message = "Email should be valid")
    private String email;

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

    public String getFullname() { return  fullname; }

    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
