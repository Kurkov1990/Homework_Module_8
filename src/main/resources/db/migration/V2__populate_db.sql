INSERT INTO CLIENT (NAME)
VALUES ('Microsoft'),
       ('Google'),
       ('Apple'),
       ('Amazon'),
       ('Tesla');

INSERT INTO WORKER (NAME, BIRTHDAY, LEVEL, SALARY)
VALUES ('John Smith', '1990-05-10', 'Senior', 5000),
       ('Alice Johnson', '1985-09-22', 'Middle', 3500),
       ('Bob Williams', '1995-12-15', 'Junior', 1500),
       ('Eve Davis', '1988-03-03', 'Senior', 5200),
       ('Charlie Brown', '1992-07-07', 'Middle', 3400);

INSERT INTO PROJECT (CLIENT_ID, NAME, START_DATE, FINISH_DATE)
VALUES (1, 'Cloud Platform', '2022-01-01', '2022-12-31'),
       (2, 'Search Engine', '2022-02-01', '2023-01-31'),
       (3, 'iOS App', '2022-03-01', '2022-09-30'),
       (4, 'E-commerce', '2022-04-01', '2023-04-01'),
       (5, 'Electric Car AI', '2022-05-01', '2023-05-01');

INSERT INTO PROJECT_WORKER (PROJECT_ID, WORKER_ID)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (3, 4),
       (4, 4),
       (4, 5),
       (5, 1),
       (5, 5);
