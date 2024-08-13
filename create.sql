-- example of the use of all the column constraints
CREATE TABLE Alumns (
    id number PRIMARY KEY,
    NAME string default "anonimo",
    edad number unique not null,
    email string check (email = "pancho@email.com"),
    fk_algo number,
    CONSTRAINT fk_algo_ref FOREIGN KEY (fk_algo) REFERENCES Algo(id)
);
