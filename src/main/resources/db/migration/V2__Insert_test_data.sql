INSERT INTO "user" (username, email, password, status)
VALUES ('user1', 'user1@example.com', 'password1', 'ACTIVE'),
       ('admin', 'admin@example.com', 'password2', 'ACTIVE'),
       ('user2', 'user2@example.com', 'password3', 'PENDING');

INSERT INTO profile (user_id, full_name, gender, date_of_birth, address, phone_number, avatar_url)
VALUES (1, 'John Doe', 'Male', '1990-01-01', '1234 Main St, Anytown', '123-456-7890',
        'https://example.com/avatar1.jpg'),
       (2, 'Jane Smith', 'Female', '1985-05-05', '5678 Oak St, Othertown', '234-567-8901',
        'https://example.com/avatar2.jpg');

INSERT INTO role (name, description)
VALUES ('USER', 'A regular user with standard privileges.'),
       ('ADMIN', 'A user with administrative privileges.');

-- Предполагаем, что 'USER' имеет ID 1 и 'ADMIN' имеет ID 2
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1), -- Пользователь 1 имеет роль USER
       (2, 2), -- Администратор имеет роль ADMIN
       (3, 1); -- Пользователь 2 имеет роль USER

INSERT INTO user_activation_token (user_id, token, expiration_time)
VALUES (1, 'abc123', '2023-12-31 23:59:59'),
       (2, 'def456', '2023-12-31 23:59:59'),
       (3, 'ghi789', '2023-12-31 23:59:59');