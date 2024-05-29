USE "/home/richy/";

CREATE TABLE example (
    id NUMBER PRIMARY KEY,
    name STRING,
    age NUMBER
);

INSERT INTO example (id, name, age) VALUES (2, 'Richy', 20);
INSERT INTO example (id, name, age) VALUES (3, 'John', 30);
INSERT INTO example (id, name, age) VALUES (4, 'Doe', 40);

SELECT * FROM example;

UPDATE example SET age = 21 WHERE name = 'Richy';

SELECT * FROM example;

DELETE FROM example WHERE name = 'John';

SELECT * FROM example;

