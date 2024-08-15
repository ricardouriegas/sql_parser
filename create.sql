-- example of the use of all the column constraints
CREATE TABLE Alumns (
    id number PRIMARY KEY,
    NAME string default "anonimo",
    edad number unique not null,
    email string check (email = "pancho@email.com"),
    fk_algo number,
    CONSTRAINT fk_algo_ref FOREIGN KEY (fk_algo) REFERENCES Algo(id)
);

insert into alumns (id, edad, email, fk_algo) values (1, 20, "nose@nose.com", 1); -- should not work bc of edad being string

update alumns set edad = 20 where id = 1; -- this will fail because the column is unique
