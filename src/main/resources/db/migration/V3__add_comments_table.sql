CREATE TABLE comments (
        id bigserial PRIMARY KEY,
        comment_content VARCHAR(5000) NOT NULL,
        date DATE,
        post_id BIGINT,
        user_id BIGINT,
        CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES posts(id),
        CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

