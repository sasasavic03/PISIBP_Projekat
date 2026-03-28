package org.instagram.user_service.dto;

public class UpdatePrivacyRequest {
    private Boolean isPrivate;

    public UpdatePrivacyRequest() {
    }

    public UpdatePrivacyRequest(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}

