INSERT INTO users (name, username, password) VALUES
    ('User', 'admin', '{noop}admin'),
    ('User', 'user', '{noop}user');

INSERT INTO users_roles (user_id, role) VALUES
    (1, 'ROLE_USER'),
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');