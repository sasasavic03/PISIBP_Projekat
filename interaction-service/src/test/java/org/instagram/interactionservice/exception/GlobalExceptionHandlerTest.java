package org.instagram.interactionservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFound_returns404() {
        ResponseEntity<Map<String, Object>> resp = handler.handleResourceNotFoundException(new ResourceNotFoundException("x"));
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals(404, resp.getBody().get("status"));
    }

    @Test
    void handleBadRequest_returns400() {
        ResponseEntity<Map<String, Object>> resp = handler.handleBadRequestException(new BadRequestException("x"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(400, resp.getBody().get("status"));
    }

    @Test
    void handleUnauthorized_returns403() {
        ResponseEntity<Map<String, Object>> resp = handler.handleUnauthorizedException(new UnauthorizedException("x"));
        assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
        assertEquals(403, resp.getBody().get("status"));
    }

    @Test
    void handleGeneric_returns500() {
        ResponseEntity<Map<String, Object>> resp = handler.handleGenericException(new RuntimeException("boom"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals(500, resp.getBody().get("status"));
    }

    @Test
    void handleValidation_returns400AndFieldMap() {
        // Build a minimal validation exception
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(new FieldError("obj", "field", "must not be blank"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException((MethodParameter) null, bindingResult);

        ResponseEntity<Map<String, Object>> resp = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Validation Failed", resp.getBody().get("error"));
        assertTrue(((Map<?, ?>) resp.getBody().get("validationErrors")).containsKey("field"));
    }
}
