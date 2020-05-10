DROP TABLE users_roles IF EXISTS;
DROP TABLE users IF EXISTS;

CREATE TABLE users
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255)            NOT NULL,
    username         VARCHAR(255)            NOT NULL,
    password         VARCHAR(255)            NOT NULL,
    UNIQUE (username)
);

CREATE TABLE users_roles
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          INTEGER                 NOT NULL,
    role             VARCHAR                 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, role)
);