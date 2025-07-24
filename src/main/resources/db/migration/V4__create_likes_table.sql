CREATE TABLE likes (
    post_id BIGINT,
    user_id BIGINT,
    CONSTRAINT fk_post FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, user_id)
);