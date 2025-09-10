INSERT INTO student (name, surname, sex, birthdate, enrolment_date, email) VALUES
('Gor', 'Barxudaryan', 'Male', '2005-02-22', '2021-09-01', 'gor1@example.com'),
('Ani', 'Hakobyan', 'Female', '2004-05-10', '2021-09-01', 'ani@example.com'),
('Aram', 'Petrosyan', 'Male', '2003-11-15', '2020-09-01', 'aram@example.com'),
('Mariam', 'Sargsyan', 'Female', '2005-03-20', '2022-02-15', 'mariam@example.com'),
('Karen', 'Vardanyan', 'Male', '2002-07-01', '2020-09-01', 'karen@example.com'),
('Lilit', 'Khachatryan', 'Female', '2005-09-17', '2021-09-01', 'lilit@example.com'),
('David', 'Martirosyan', 'Male', '2004-12-30', '2021-09-01', 'david@example.com'),
('Sona', 'Avetisyan', 'Female', '2003-06-12', '2019-09-01', 'sona@example.com'),
('Tigran', 'Simonyan', 'Male', '2002-01-25', '2018-09-01', 'tigran@example.com'),
('Narine', 'Grigoryan', 'Female', '2004-08-09', '2020-09-01', 'narine@example.com');


INSERT INTO professor (name, surname, department) VALUES
('Hayk', 'Manukyan', 'Mathematics'),
('Armen', 'Davtyan', 'Physics'),
('Suren', 'Petrosyan', 'Computer Science'),
('Narek', 'Avagyan', 'Biology'),
('Anna', 'Karapetyan', 'Chemistry'),
('Levon', 'Grigoryan', 'Philosophy'),
('Marine', 'Vardanyan', 'English Literature'),
('Samvel', 'Khachatryan', 'History'),
('Vahan', 'Hakobyan', 'Programming'),
('Gayane', 'Sargsyan', 'Databases');


INSERT INTO course (title, credits, professor_id) VALUES
('Mathematics I', 5, 1),
('Physics I', 4, 2),
('Computer Science', 6, 3),
('Biology', 3, 4),
('Chemistry', 4, 5),
('Philosophy', 2, 6),
('English Literature', 3, 7),
('History of Armenia', 3, 8),
('Programming Java', 5, 9),
('Databases', 5, 10);

INSERT INTO assignment (title, due_date, course_id) VALUES
('HW1 - Algebra', '2025-09-10', 1),
('Lab1 - Mechanics', '2025-09-12', 2),
('Project - Website', '2025-09-15', 3),
('Lab2 - Cells', '2025-09-20', 4),
('Quiz1 - Organic', '2025-09-22', 5),
('Essay - Logic', '2025-09-25', 6),
('Book Review', '2025-09-27', 7),
('Essay - History', '2025-09-29', 8),
('HW2 - OOP', '2025-10-01', 9),
('DB Schema Project', '2025-10-05', 10);


INSERT INTO submission (student_id, submitted_at, assignment_id, grade) VALUES
(1, '2025-09-10', 1, 85),
(2, '2025-09-11', 2, 90),
(3, '2025-09-15', 3, 78),
(4, '2025-09-21', 4, 88),
(5, '2025-09-22', 5, 92),
(6, '2025-09-26', 6, 80),
(7, '2025-09-27', 7, 95),
(8, '2025-09-29', 8, 70),
(9, '2025-10-01', 9, 100),
(10, '2025-10-05', 10, 85);


INSERT INTO student_course (student_id, course_id) VALUES
(1, 1),
(1, 3),
(2, 2),
(2, 5),
(3, 3),
(3, 9),
(4, 4),
(5, 1),
(5, 10),
(6, 7),
(7, 2),
(7, 9),
(8, 8),
(9, 9),
(9, 10),
(10, 5),
(10, 7);
