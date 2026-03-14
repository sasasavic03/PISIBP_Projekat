-- Create comments table
CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    content VARCHAR(2200) NOT NULL,
    is_edited BOOLEAN DEFAULT false,
    is_deleted BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_comment_post_id ON comments(post_id);
CREATE INDEX IF NOT EXISTS idx_comment_user_id ON comments(user_id);
CREATE INDEX IF NOT EXISTS idx_comment_created_at ON comments(created_at);
CREATE INDEX IF NOT EXISTS idx_comment_is_deleted ON comments(is_deleted);
