package edu.upvictoria.fpoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Somthing i need to say:
 * I take order by has order_by (with the underscore) the same with NOT_NULL
 * And i make all the sentence in uppercase
 * This is a First Practice and it doesnt need to validate things so dates are not validated to be treaten as dates
 * I use only the doble quotes ("") for the strings not the single quotes ('')
 */

/**
 * Example of some sentences:
 * CREATE TABLE alumnos (nombre "STRING" PRIMARY_KEY, edad 1 NOT_NULL,
 * fecha_nacimiento "10/10/1001" NULL);
 * INSERT INTO alumnos (nombre, edad, fecha_nacimiento) VALUES ("Juan", 20,
 * "2000-01-01");
 * SELECT nombre, edad FROM alumnos WHERE edad > 18 ORDER_BY edad DESC LIMIT 10;
 * UPDATE alumnos SET edad = 21 WHERE nombre = "Juan";
 * DELETE FROM alumnos WHERE nombre = "Juan";
 * DROP TABLE alumnos;
 * select sum(salario) div count(*) where apellido = "hernandez";
 */
public class App {
    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) { // to run with prompting
            runPrompt();
        } else if (args.length == 1) { // to run a file like in python
            runFile(args[0]);
        }
        // else { // to run a file but with the -D and -f flags
        // runFile(args);
        // }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            try {
                // Lex the given clause
                Lexer lexer = new Lexer(line);
                List<Token> tokens = lexer.scanTokens();

                // Parse the token given by the lexer
                Parser parser = new Parser(tokens);
                List<Clause> expressions = parser.parse();

                // For now Print the AST
                // for (Clause clause : expressions) {
                // System.out.println(clause.accept(new AstPrinter()));
                // }

                // Interpret the given by the parser
                for (Clause expression : expressions) {
                    interpreter.interpret(expression);
                }
            } catch (Error e) {
                System.err.println(e.getMessage());
            } 
            catch (Exception e) {
                System.err.println("Something goes wrong.");
            }
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Clause> clauses = parser.parse();

        // Interpret the given by the parser
        for (Clause clause : clauses) {
            // print the clause
            interpreter.interpret(clause);
            System.out.println();
        }
    }

    /**
     * for now we are not going to use this method (i prefer to use the other one,
     * is just simpler and intuitive)
     * private static void runFile(String[] args) throws IOException {
     * // `java App -D[0] path[1] -f[2] file.sql[3]
     * // -D /home/richy/ -f /home/richy/ejemplo.sql
     * 
     * if (args[0].equals("-D") && args[2].equals("-f")) {
     * Path folder = Paths.get(args[1]);
     * 
     * if (!Files.exists(folder)) {
     * Files.createDirectory(folder);
     * }
     * 
     * // Add the file to the folder
     * // Path file = Paths.get(args[1] + args[3]);
     * 
     * 
     * // interpret USE_CLAUSE with the path of the database
     * Lexer algo = new Lexer("USE \"" + args[1] + "\";");
     * List<Token> tokens2 = algo.scanTokens();
     * Parser parser2 = new Parser(tokens2);
     * Clause expression2 = parser2.parse();
     * interpreter.interpret(expression2);
     * 
     * 
     * 
     * System.out.println("Everything was executed");
     * 
     * } else {
     * ErrorHandler.error("Invalid arguments");
     * }
     * 
     * }
     */

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        try {
            run(new String(bytes, Charset.defaultCharset()));
        } catch (Error e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Something goes wrong.");
        }

    }

}
