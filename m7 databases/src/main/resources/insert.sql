PRAGMA
foreign_keys = ON;

DELETE
FROM students;
DELETE
FROM courses;

INSERT INTO courses (course_name, credits)
VALUES ('KURSAI1', 4);
INSERT INTO courses (course_name, credits)
VALUES ('KURSAI2', 3);
INSERT INTO courses (course_name, credits)
VALUES ('KURSAI3', 3);

INSERT INTO students (student_name, student_email, student_age, course_id)
VALUES ('John', 'jon@as', 20, 1);
INSERT INTO students (student_name, student_email, student_age, course_id)
VALUES ('Jane', 'jan@as', 21, 2);
INSERT INTO students (student_name, student_email, student_age, course_id)
VALUES ('Jill', 'jill@as', 18, 3);
INSERT INTO students (student_name, student_email, student_age, course_id)
VALUES ('Jack', 'jack@as', 18, 3);
INSERT INTO students (student_name, student_email, student_age, course_id)
VALUES ('Joana', 'joan@as', 18, 2);