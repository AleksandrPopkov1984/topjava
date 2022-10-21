DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2022-10-20 08:00:00-00', 'Завтрак', 500, 100001),
       ('2022-10-20 19:30:45-07', 'Ужин', 1000, 100001),
       ('2022-10-21 08:00:00-00', 'Завтрак', 500, 100000),
       ('2022-10-21 19:30:45-07', 'Ужин', 1000, 100000);


