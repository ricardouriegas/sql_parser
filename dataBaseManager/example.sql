USE "/home/richy/";

-- select distinct max(EMPLOYEE_ID) from EMPLOYEES; -- should result in an error if employees doesn'n exist

CREATE TABLE example ( -- should return an error if example already exists
    id NUMBER PRIMARY KEY,
    name STRING,
    age NUMBER
);

INSERT INTO example (id, name, age) VALUES (1, 'Richy', 20);
INSERT INTO example (id, name, age) VALUES (3, 'John', 30);
INSERT INTO example (id, name, age) VALUES (4, 'Doe', 40);
INSERT INTO example (id, name, age) VALUES (5, 'Jane', 50);
INSERT INTO example (id, name, age) VALUES (6, 'Doe', 60);
INSERT INTO example (id, name, age) VALUES (7, 'Jane', 70);
INSERT INTO example (id, name, age) VALUES (8, 'Doe', 80);
INSERT INTO example (id, name, age) VALUES (9, 'Jane', 90);
INSERT INTO example (id, name, age) VALUES (10, 'Doe', 100);
INSERT INTO example (id, name, age) VALUES (11, 'Jane', 110);
INSERT INTO example (id, name, age) VALUES (12, 'Doe', 120);
INSERT INTO example (id, name, age) VALUES (13, 'Jane', 130);
INSERT INTO example (id, name, age) VALUES (14, 'Doe', 140);
INSERT INTO example (id, name, age) VALUES (15, 'Jane', 150);
INSERT INTO example (id, name, age) VALUES (41, 'Doe', 160);
INSERT INTO example (id, name, age) VALUES (42, 'Jane', 170);
INSERT INTO example (id, name, age) VALUES (43, 'Doe', 180);
INSERT INTO example (id, name, age) VALUES (44, 'Jane', 190);
INSERT INTO example (id, name, age) VALUES (45, 'Doe', 200);

SELECT * FROM example;

UPDATE example SET age = 21 WHERE name = "Richy";

SELECT * FROM example;

DELETE FROM example WHERE name = 'John';

SELECT * FROM example;

select COUNT(FLOOR(ID)) from example;

select (2 mod 4) / 8 *2;

select count(*) from example;

update example set id = max(id) where id = 41;  

insert into example (id, name, age) values (41 + 8, 'Richy', round(20.5));

-- select id, name, nose from example; -- should result in an error (nose doesnt exist in the table)

-- select id from example where max(id) = 1; -- should return an error bc of the max function

-- Aggregation functions
select count(*) from example;
select sum(age) from example;
select avg(age) from example;
select max(age) from example;
select min(age) from example;

-- drop table example;

select max(name) from example; -- should return the greatest lexical value
select min(name) from example; -- should return the smallest lexical value

select max(age) from example; -- should return the greatest numerical value
select min(age) from example; -- should return the smallest numerical value

-- select avg(name) from example; -- should return an error bc name is not a numerical value
-- select round(name) from example; -- should return an error bc name is not a numerical value

select count(*) from example where name = 'Doe';

select distinct name from example;