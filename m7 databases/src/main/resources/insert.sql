PRAGMA foreign_keys = ON;

-- Delete students first because students depends on courses.
DELETE FROM students;
DELETE FROM courses;

-- TODO 1:
    INSERT INTO courses (course_name, credits) VALUES ('KURSAI1', 4);
    INSERT INTO courses (course_name, credits) VALUES ('KURSAI2', 3);
    INSERT INTO courses (course_name, credits) VALUES ('KURSAI3', 3);

-- TODO 2:

-- Insert 5 students.
-- Remember:
-- id is a number.
-- name must be present.
-- email must be unique.
-- age must be 18 or older.
-- course_id must exist in the courses table.