Interaction-service za lajkovanje i komentarisanje

HELLO


## API Endpoints 

Likes API
- `POST /api/likes` - Like a post
- `DELETE /api/likes` - Unlike a post
- `GET /api/likes/post/{postId}` - Get post likes
- `GET /api/likes/post/{postId}/count` - Get like count
- `GET /api/likes/check` - Check if user liked post
- `GET /api/likes/user/{userId}` - Get user's likes
- `GET /api/likes/health` - Health check

Comments API
- `POST /api/comments` - Add a comment
- `GET /api/comments/post/{postId}` - Get post comments
- `GET /api/comments/{commentId}` - Get specific comment
- `GET /api/comments/post/{postId}/count` - Get comment count
- `PATCH /api/comments/{commentId}` - Update comment
- `DELETE /api/comments/{commentId}` - Delete comment
- `GET /api/comments/health` - Health check


### TABELE U BAZI
Likes Table
- `id` (BIGINT, Primary Key)
- `user_id` (BIGINT, NOT NULL)
- `post_id` (BIGINT, NOT NULL)
- `created_at` (TIMESTAMP, NOT NULL)
- Unique constraint on `(user_id, post_id)`

Comments Table
- `id` (BIGINT, Primary Key)
- `user_id` (BIGINT, NOT NULL)
- `post_id` (BIGINT, NOT NULL)
- `content` (VARCHAR(2200), NOT NULL)
- `is_edited` (BOOLEAN, default false)
- `is_deleted` (BOOLEAN, default false)
- `created_at` (TIMESTAMP, NOT NULL)
- `updated_at` (TIMESTAMP)
