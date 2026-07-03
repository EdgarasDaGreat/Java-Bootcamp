PRAGMA
foreign_keys = ON;

SELECT *
FROM courses;

SELECT *
FROM students;

SELECT *
FROM students
WHERE student_age > 20;

SELECT students.student_name, courses.course_name
FROM students
         JOIN courses
              ON students.course_id = courses.course_id;

SELECT course_id, COUNT(student_id) AS student_count
FROM students
GROUP BY course_id;

UPDATE students
SET student_age = student_age + 1
WHERE student_id = 1;

UPDATE students
SET course_id = 1
WHERE student_id = 2;

SELECT course_id, COUNT(student_id) AS student_count
FROM students
WHERE student_id = 2
GROUP BY course_id;

DELETE
FROM students
WHERE student_id = 4;

SELECT *
FROM students;