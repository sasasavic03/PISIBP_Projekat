package org.instagram.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

/**
 * Base test class for all API integration tests
 */
public abstract class BaseApiTest {

    protected static final String AUTH_SERVICE_URL = "http://localhost:8081/auth";
    protected static final String USER_SERVICE_URL = "http://localhost:8082/users";
    protected static final String POST_SERVICE_URL = "http://localhost:8083/api/posts";
    protected static final String FOLLOW_SERVICE_URL = "http://localhost:8084/api/follow";
    protected static final String BLOCK_SERVICE_URL = "http://localhost:8085/api/users";
    protected static final String NOTIFICATION_SERVICE_URL = "http://localhost:8086/api/notification";
    protected static final String FEED_SERVICE_URL = "http://localhost:8087/api/feed";
    protected static final String INTERACTION_SERVICE_URL = "http://localhost:8088/api";
    protected static final String API_GATEWAY_URL = "http://localhost:8080";

    @BeforeAll
    public static void setup() {
        // REST Assured setup
    }

    protected static void setDefaultHeaders() {
        RestAssured.requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    protected static void clearAuth() {
        RestAssured.reset();
    }
}

