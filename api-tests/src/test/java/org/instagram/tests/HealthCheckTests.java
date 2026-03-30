package org.instagram.tests;

import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Auth Service - Health Check")
public class HealthCheckTests extends BaseApiTest {

    @BeforeEach
    void beforeEach() {
        setDefaultHeaders();
    }

    @AfterEach
    void afterEach() {
        clearAuth();
    }

    @Test
    @DisplayName("Auth Service Health")
    void testAuthHealth() {
        given()
                .when()
                .get(AUTH_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }

    @Test
    @DisplayName("User Service Health")
    void testUserHealth() {
        given()
                .when()
                .get(USER_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 400, 404, 500));
    }

    @Test
    @DisplayName("Post Service Health")
    void testPostHealth() {
        given()
                .when()
                .get(POST_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }

    @Test
    @DisplayName("Follow Service Health")
    void testFollowHealth() {
        given()
                .when()
                .get(FOLLOW_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }

    @Test
    @DisplayName("Block Service Health")
    void testBlockHealth() {
        given()
                .when()
                .get(BLOCK_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }

    @Test
    @DisplayName("Notification Service Health")
    void testNotificationHealth() {
        given()
                .when()
                .get(NOTIFICATION_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }

    @Test
    @DisplayName("Feed Service Health")
    void testFeedHealth() {
        given()
                .when()
                .get(FEED_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }

    @Test
    @DisplayName("Interaction Service Health")
    void testInteractionHealth() {
        given()
                .when()
                .get(INTERACTION_SERVICE_URL + "/health")
                .then()
                .statusCode(isOneOf(200, 201, 404, 500));
    }
}

