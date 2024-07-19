drop table if exists payment;
drop table if exists horse;
drop table if exists student;
drop table if exists class;
drop table if exists horse_class;
drop table if exists student_class;
drop table if exists teacher;
drop table if exists teacher_class;


CREATE TABLE if not exists payment
(
    id          SERIAL PRIMARY KEY,
    amount      DECIMAL NOT NULL,
    payment_date        DATE    NOT NULL
);


CREATE TABLE if not exists horse
(
    id          SERIAL PRIMARY KEY,
    horse_name  VARCHAR NOT NULL,
    color       VARCHAR NOT NULL,
    height      DECIMAL NOT NULL,
    birth_date  DATE    NOT NULL
);

CREATE TABLE if not exists student
(
    id            SERIAL PRIMARY KEY,
    student_name  VARCHAR NOT NULL,
    birth_date    DATE NOT NULL,
    nif           INT NOT NULL,
    password      VARCHAR NOT NULL,
    email         VARCHAR NOT NULL,
    phone_number  INT NOT NULL,
    description   VARCHAR NOT NULL
);

CREATE TABLE if not exists class
(
    id          SERIAL PRIMARY KEY,
    class_name  VARCHAR NOT NULL,
    class_date  TIMESTAMP    NOT NULL,
    done        BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE if not exists horse_class
(
    horse_id  INT REFERENCES Horse (id),
    class_id  INT REFERENCES Class (id),
    primary key (horse_id, class_id)
);


CREATE TABLE if not exists student_class
(
    student_id  INT REFERENCES Student (id),
    class_id    INT REFERENCES Class (id),
    primary key (student_id, class_id)
);

CREATE TABLE if not exists teacher
(
    id            SERIAL PRIMARY KEY,
    teacher_name  VARCHAR NOT NULL,
    password      VARCHAR NOT NULL,
    email         VARCHAR NOT NULL,
    phone_number  INT NOT NULL
);


CREATE TABLE if not exists teacher_class
(
    teacher_id  INT REFERENCES Teacher (id),
    class_id    INT REFERENCES Class (id),
    primary key (teacher_id, class_id)
);

