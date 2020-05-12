DROP TABLE users_roles IF EXISTS;
DROP TABLE users IF EXISTS;
DROP SEQUENCE users_seq IF EXISTS;

create sequence users_seq;

CREATE TABLE users
(
    id                      BIGINT DEFAULT users_seq.nextval PRIMARY KEY,
    name                    VARCHAR(255)            NOT NULL,
    username                VARCHAR(255)            NOT NULL,
    password                VARCHAR(255)            NOT NULL,
    telegram_chat_id        BIGINT,
    UNIQUE (username),
    UNIQUE (telegram_chat_id)
);

CREATE TABLE users_roles
(
    id               BIGINT DEFAULT users_seq.nextval PRIMARY KEY,
    user_id          INTEGER                 NOT NULL,
    role             VARCHAR                 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, role)
);