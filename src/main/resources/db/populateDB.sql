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
       ('2022-10-20 08:30:00-00', 'Завтрак', 650, 100000),
       ('2022-10-20 12:45:00-00', 'Обед', 1200, 100001),
       ('2022-10-20 13:00:00-00', 'Обед', 1500, 100000),
       ('2022-10-20 19:30:00-00', 'Ужин', 1000, 100001),
       ('2022-10-20 20:00:00-00', 'Ужин', 800, 100000),
       ('2022-10-21 06:00:00-00', 'Завтрак', 500, 100000),
       ('2022-10-21 12:10:00-00', 'Обед', 1000, 100000),
       ('2022-10-21 18:19:00-00', 'Ужин', 400, 100000),
       ('2022-10-21 7:12:00-00', 'Завтрак', 450, 100001),
       ('2022-10-21 13:34:00-00', 'Обед', 1100, 100001),
       ('2022-10-21 19:25:00-00', 'Ужин', 200, 100001);


