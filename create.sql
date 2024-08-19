-- example of the use of all the column constraints
CREATE TABLE Algo (
    id number PRIMARY KEY,
    NAME string default "anonimo",
    edad number not null,
    email string unique
);

CREATE TABLE Alumns (
    id number PRIMARY KEY,
    NAME string default "anonimo",
    edad number unique not null,
    email string unique,
    fk_algo number,
    CONSTRAINT fk_algo_ref FOREIGN KEY (fk_algo) REFERENCES Algo(id)
);

-- insert and update in Algo
insert into algo (id, edad, email) values (1, 20, "nose"); -- this will work

insert into alumns (id, edad, email, fk_algo) values (1, 20, "pancho1@email.com", 1); -- should work 
insert into alumns (id, edad, email, fk_algo) values (2, 30, "pancho2@email.com", 1); -- should work
insert into alumns (id, email, fk_algo) values (2, "pancho2@email.com", 1); -- should NOT work

update alumns set edad = 20 where id = 1; -- this will fail because the column is unique
update alumns set edad = 40 where id = 2; -- this will work

-- test simple alter 
ALTER TABLE Alumns ADD COLUMN apellido string;
ALTER TABLE alumns MODIFY COLUMN apellido string not null;

-- insert and update in apellido
insert into alumns (id, edad, email, fk_algo, apellido) values (3, 30, "panchin", 1, "perez"); -- this will work
update alumns set apellido = "perez2" where id = 3; -- this will work
-- one that should not work
insert into alumns (id, edad, email, fk_algo, apellido) values (4, 30, "panchin", 1, null); -- this will not work bc of not null

update alumns set apellido = null where id = 3; -- this will not work bc of not null

insert into alumns (id, edad, email, fk_algo, apellido) values (6, 25, "panchin2", 1, "perez"); -- this will work
insert into alumns (id, edad, email, fk_algo, apellido) values (5, 30, "panchin", 1, "perez"); -- this will work

-- drop column
ALTER TABLE alumns DROP COLUMN apellido;