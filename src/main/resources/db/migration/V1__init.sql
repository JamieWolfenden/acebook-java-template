DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id bigserial PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  enabled boolean NOT NULL
);

DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
  id bigserial PRIMARY KEY,
  content varchar(250) NOT NULL,
  date DATE,
  like_count INT,
  user_id BIGINT,
  CONSTRAINT fk_user_post
  FOREIGN KEY (user_id)
  REFERENCES users(id)
  ON DELETE CASCADE
);
