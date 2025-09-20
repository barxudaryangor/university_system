CREATE TABLE student(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(20),
    surname varchar(20),
    sex varchar(20),
    birthdate DATE,
    enrolment_date DATE,
    email varchar(30)
);

CREATE TABLE professor (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(20),
    surname varchar(20),
    department varchar(20)
);

CREATE TABLE course (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar(20),
    credits SMALLINT,
    professor_id BIGINT,

    CONSTRAINT fk_course_professor FOREIGN KEY (professor_id) REFERENCES professor(id)
);

CREATE TABLE assignment (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar(20),
    due_date DATE,
    course_id BIGINT,


    CONSTRAINT fk_assignment_course FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE submission (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id BIGINT,
    submitted_at DATE,
    assignment_id BIGINT,
    grade decimal,

    CONSTRAINT fk_submission_student FOREIGN KEY (student_id) REFERENCES student(id) ,
    CONSTRAINT fk_submission_assignment FOREIGN KEY (assignment_id) REFERENCES assignment(id)
);

CREATE TABLE student_course (
    student_id BIGINT,
    course_id BIGINT,
    PRIMARY KEY (student_id, course_id),
    CONSTRAINT fk_sc_student FOREIGN KEY (student_id) REFERENCES student(id) ,
    CONSTRAINT fk_sc_course FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL
);
