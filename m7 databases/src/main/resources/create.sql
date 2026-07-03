PRAGMA
foreign_keys = ON;

DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;

CREATE TABLE courses
(
    course_id   INTEGER PRIMARY KEY,
    course_name TEXT    NOT NULL,
    credits     INTEGER NOT NULL CHECK (credits > 0)
);

-- TODO 2:
CREATE TABLE students
(
    student_id    INTEGER PRIMARY KEY,
    student_name  TEXT    NOT NULL,
    student_email TEXT    NOT NULL UNIQUE,
    student_age   INTEGER NOT NULL CHECK (student_age >= 18),
    course_id     INTEGER NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);