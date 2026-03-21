package org.instagram.apigateway.exception;

public class MissingAuthorizationException extends RuntimeException {
    public MissingAuthorizationException(String message) {
        super(message);
    }
}
