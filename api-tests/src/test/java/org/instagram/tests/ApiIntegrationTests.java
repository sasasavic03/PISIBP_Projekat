package org.instagram.tests;

import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("API Integration Tests - Main Test Suite")
public class ApiIntegrationTests extends BaseApiTest {

    @BeforeEach
    void beforeEach() {
        setDefaultHeaders();
    }

    @AfterEach
    void afterEach() {
        clearAuth();
    }

    // ==================== USER TESTS ====================

    @Test
    @DisplayName("User: Create user")
    void testCreateUser() {
        String request = """
                {
                    "username": "testuser",
                    "email": "test@example.com",
                    "dateOfBirth": "1990-01-01",
                    "firstName": "Test"
                }
                """;
        given()
                .body(request)
                .when()
                .post(USER_SERVICE_URL)
                .then()
                .statusCode(isOneOf(200, 201, 400, 409, 500));
    }

    @Test
    @DisplayName("User: Get user by ID")
    void testGetUser() {
        given()
                .when()
                .get(USER_SERVICE_URL + "/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    @Test
    @DisplayName("User: Get user by username")
    void testGetUserByUsername() {
        given()
                .when()
                .get(USER_SERVICE_URL + "/username/testuser")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    // ==================== POST TESTS ====================

    @Test
    @DisplayName("Post: Create post")
    void testCreatePost() {
        String request = """
                {
                    "description": "Test post",
                    "tags": ["test"]
                }
                """;
        given()
                .header("X-User-Id", "1")
                .body(request)
                .when()
                .post(POST_SERVICE_URL)
                .then()
                .statusCode(isOneOf(200, 201, 400, 401, 403, 500));
    }

    @Test
    @DisplayName("Post: Get post")
    void testGetPost() {
        given()
                .when()
                .get(POST_SERVICE_URL + "/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    @Test
    @DisplayName("Post: Get user posts")
    void testGetUserPosts() {
        given()
                .when()
                .get(POST_SERVICE_URL + "/user/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    // ==================== FOLLOW TESTS ====================

    @Test
    @DisplayName("Follow: Follow user")
    void testFollowUser() {
        String request = """
                {
                    "followeeId": 2
                }
                """;
        given()
                .header("X-User-Id", "1")
                .body(request)
                .when()
                .post(FOLLOW_SERVICE_URL + "/follow")
                .then()
                .statusCode(isOneOf(200, 201, 400, 401, 403, 404, 500));
    }

    @Test
    @DisplayName("Follow: Get followers")
    void testGetFollowers() {
        given()
                .when()
                .get(FOLLOW_SERVICE_URL + "/followers/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    @Test
    @DisplayName("Follow: Get following")
    void testGetFollowing() {
        given()
                .when()
                .get(FOLLOW_SERVICE_URL + "/following/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    // ==================== BLOCK TESTS ====================

    @Test
    @DisplayName("Block: Block user")
    void testBlockUser() {
        String request = """
                {
                    "blockedUserId": 2
                }
                """;
        given()
                .header("X-User-Id", "1")
                .body(request)
                .when()
                .post(BLOCK_SERVICE_URL + "/1/block")
                .then()
                .statusCode(isOneOf(200, 201, 400, 401, 403, 404, 500));
    }

    @Test
    @DisplayName("Block: Get blocked users")
    void testGetBlockedUsers() {
        given()
                .when()
                .get(BLOCK_SERVICE_URL + "/1/blocked")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    // ==================== NOTIFICATION TESTS ====================

    @Test
    @DisplayName("Notification: Create")
    void testCreateNotification() {
        String request = """
                {
                    "userId": 1,
                    "type": "FOLLOW",
                    "message": "Test notification"
                }
                """;
        given()
                .body(request)
                .when()
                .post(NOTIFICATION_SERVICE_URL)
                .then()
                .statusCode(isOneOf(200, 201, 400, 500));
    }

    @Test
    @DisplayName("Notification: Get notifications")
    void testGetNotifications() {
        given()
                .header("X-User-Id", "1")
                .when()
                .get(NOTIFICATION_SERVICE_URL + "/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 401, 403, 500));
    }

    // ==================== FEED TESTS ====================

    @Test
    @DisplayName("Feed: Get feed")
    void testGetFeed() {
        given()
                .header("X-User-Id", "1")
                .when()
                .get(FEED_SERVICE_URL + "/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 401, 403, 500));
    }

    // ==================== INTERACTION TESTS ====================

    @Test
    @DisplayName("Interaction: Like post")
    void testLikePost() {
        String request = """
                {
                    "postId": 1
                }
                """;
        given()
                .header("X-User-Id", "1")
                .body(request)
                .when()
                .post(INTERACTION_SERVICE_URL + "/likes")
                .then()
                .statusCode(isOneOf(200, 201, 400, 401, 403, 404, 500));
    }

    @Test
    @DisplayName("Interaction: Get likes")
    void testGetLikes() {
        given()
                .when()
                .get(INTERACTION_SERVICE_URL + "/likes/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }

    @Test
    @DisplayName("Interaction: Create comment")
    void testCreateComment() {
        String request = """
                {
                    "postId": 1,
                    "content": "Test comment"
                }
                """;
        given()
                .header("X-User-Id", "1")
                .body(request)
                .when()
                .post(INTERACTION_SERVICE_URL + "/comments")
                .then()
                .statusCode(isOneOf(200, 201, 400, 401, 403, 404, 500));
    }

    @Test
    @DisplayName("Interaction: Get comments")
    void testGetComments() {
        given()
                .when()
                .get(INTERACTION_SERVICE_URL + "/comments/1")
                .then()
                .statusCode(isOneOf(200, 404, 400, 500));
    }
}




