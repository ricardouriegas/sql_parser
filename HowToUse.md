# SQL Parser Applied to a CSV

You will need to import the libary

## Lexer

To use this library you'll need to create a lexer, this because the parser parse just tokens. The lexer will receive the sql clause and return the List of tokens.

## Parser

The parser will receive a List of tokens (has a said the lexer create this list of tokens), parse the list of tokens and will return a List of clauses.

### Clause

A clause is just basic information about the sql statement, like the type of statement, the columns, the table, the where expression, etc.

## Interpreter

The interpreter will receive a clause and will interpret the clause, this means that the interpreter will execute only one clause.

## Error Handling

The libary has a error handling, if the lexer found a invalid token, the parser found a invalid clause or the interpreter found a invalid clause, the libary will throw a error, you will need to catch this error and print the message of the error; has you can see in the next example:

## Example

```Java

InputStreamReader input = new InputStreamReader(System.in);
BufferedReader reader = new BufferedReader(input);
Interpreter interpreter = new Interpreter();

for (;;) {
    System.out.print("> ");
    String line = reader.readLine();
    if (line == null)
        break;
    try {
        // Lex the clause given
        Lexer lexer = new Lexer(line);
        List<Token> tokens = lexer.scanTokens();

        // Parse the token given by the lexer
        Parser parser = new Parser(tokens);
        Clause expression = parser.parse();

        // Interpret the given by the parser
        interpreter.interpret(expression);
    } catch (Error e) {
        System.err.println(e.getMessage());
    } catch (Exception e) {
        System.err.println("Something goes wrong.");
    }
}

```

## How to use the read file

You can use the command `java App -D DataBase -f file.sql` to read a file.sql with a set of commands: use, create table, drop table, insert into, update and select. Each set of commands should be separated by ';'.
