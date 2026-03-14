-- Create likes table
CREATE TABLE IF NOT EXISTS likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, post_id)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_like_post_id ON likes(post_id);
CREATE INDEX IF NOT EXISTS idx_like_user_id ON likes(user_id);
CREATE INDEX IF NOT EXISTS idx_like_created_at ON likes(created_at);
