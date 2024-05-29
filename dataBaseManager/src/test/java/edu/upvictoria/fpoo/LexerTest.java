package edu.upvictoria.fpoo;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static edu.upvictoria.fpoo.TokenType.*;

public class LexerTest {
        /**
         * Test basic SELECT
         */
        @Test
        public void testScanTokens() {
                Lexer lexer = new Lexer("SELECT * from table;");
                List<Token> tokens = lexer.scanTokens();
                // System.out.println(tokens);
                // Expected:
                // SELECT SELECT null, FROM FROM null, TABLE TABLE null, SEMICOLON ; null, EOF
                // null
                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SELECT, "SELECT", "SELECT", 1));
                expected.add(
                                new Token(STAR, "*", null, 1));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

        /**
         * Test CREATE TABLE (with definition columns)
         */
        @Test
        public void testScanTokens2() {
                Lexer lexer = new Lexer(
                                "CREATE TABLE Alumnos (id NUMBER NOT NULL PRIMARY KEY, nombre STRING NOT NULL, app STRING NOT NULL, apm STRING NOT NULL, edad INT, fecha DATE);");
                List<Token> tokens = lexer.scanTokens();

                /**
                 * CREATE TABLE Alumnos (
                 * id INT NOT NULL PRIMARY KEY,
                 * nombre VARCHAR(20) NOT NULL,
                 * app VARCHAR(20) NOT_NULL,
                 * apm VARCHAR(20) NOT_NULL,
                 * edad INT NULL
                 * );
                 */

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(CREATE, "CREATE", "CREATE", 1));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 1));
                expected.add(
                                new Token(IDENTIFIER, "ALUMNOS", "ALUMNOS", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "ID", "ID", 1));
                expected.add(
                                new Token(NUMBER_DATA_TYPE, "NUMBER", "NUMBER", 1));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1));
                expected.add(
                                new Token(NULL, "NULL", "NULL", 1));
                expected.add(
                                new Token(PRIMARY, "PRIMARY", "PRIMARY", 1));
                expected.add(
                                new Token(KEY, "KEY", "KEY", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "NOMBRE", "NOMBRE", 1));
                expected.add(
                                new Token(STRING_DATA_TYPE, "STRING", "STRING", 1));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1));
                expected.add(
                                new Token(NULL, "NULL", "NULL", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "APP", "APP", 1));
                expected.add(
                                new Token(IDENTIFIER, "STRING", "STRING", 1));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1));
                expected.add(
                                new Token(NULL, "NULL", "NULL", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "APM", "APM", 1));
                expected.add(
                                new Token(IDENTIFIER, "STRING", "STRING", 1));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1));
                expected.add(
                                new Token(NULL, "NULL", "NULL", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "EDAD", "EDAD", 1));
                expected.add(
                                new Token(IDENTIFIER, "INT", "INT", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }

                // System.out.println(tokens);
        }

        /**
         * Test SELECT (normal query)
         */
        @Test
        public void testScanTokens3() {
                Lexer lexer = new Lexer(
                                "SELECT * FROM table WHERE id = 1;");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SELECT, "SELECT", "SELECT", 1));
                expected.add(
                                new Token(STAR, "*", null, 1));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 1));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1));
                expected.add(
                                new Token(IDENTIFIER, "ID", "ID", 1));
                expected.add(
                                new Token(EQUAL, "=", null, 1));
                expected.add(
                                new Token(NUMBER, "1", 1.0, 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

        /**
         * Test SELECT (hard query)
         */
        @Test
        public void testScanTokens4() {
                /**
                 * SELECT
                 * employee_id,
                 * first_name,
                 * last_name,
                 * department_name
                 * FROM
                 * employees
                 * WHERE
                 * salary > 50000
                 * AND department_name = "Engineering"
                 * ORDER_BY
                 * last_name ASC,
                 * first_name ASC;
                 */
                Lexer lexer = new Lexer(
                                "SELECT employee_id, first_name, last_name, department_name FROM employees WHERE salary > 50000 AND department_name = \"Engineering\" ORDER_BY last_name ASC, first_name ASC;");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SELECT, "SELECT", "SELECT", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEE_ID", "EMPLOYEE_ID", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "FIRST_NAME", "FIRST_NAME", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "LAST_NAME", "LAST_NAME", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(GREATER, ">", null, 1));
                expected.add(
                                new Token(NUMBER, "50000", 50000.0, 1));
                expected.add(
                                new Token(AND, "AND", "AND", 1));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1));
                expected.add(
                                new Token(EQUAL, "=", null, 1));
                expected.add(
                                new Token(STRING, "\"Engineering\"", "Engineering", 1));
                expected.add(
                                new Token(ORDER, "ORDER", "ORDER", 1));
                expected.add(
                                new Token(BY, "BY", "BY", 1));
                expected.add(
                                new Token(IDENTIFIER, "LAST_NAME", "LAST_NAME", 1));
                expected.add(
                                new Token(ASC, "ASC", "ASC", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "FIRST_NAME", "FIRST_NAME", 1));
                expected.add(
                                new Token(ASC, "ASC", "ASC", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }

        }

        /**
         * Test UPDATE
         */
        @Test
        public void testScanTokens5() {
                Lexer lexer = new Lexer(
                                "UPDATE employees SET salary = 60000 WHERE department_name = \"Engineering\";");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(UPDATE, "UPDATE", "UPDATE", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1));
                expected.add(
                                new Token(SET, "SET", "SET", 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(EQUAL, "=", null, 1));
                expected.add(
                                new Token(NUMBER, "60000", 60000.0, 1));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1));
                expected.add(
                                new Token(EQUAL, "=", null, 1));
                expected.add(
                                new Token(STRING, "\"Engineering\"", "Engineering", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

        /**
         * Test DELETE
         */
        @Test
        public void testScanTokens6() {
                Lexer lexer = new Lexer(
                                "DELETE FROM employees WHERE department_name = \"Engineering\";");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(DELETE, "DELETE", "DELETE", 1));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1));
                expected.add(
                                new Token(EQUAL, "=", null, 1));
                expected.add(
                                new Token(STRING, "\"Engineering\"", "Engineering", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

        /**
         * Test using the
         * MOD, SHOW, DIV, UCASE, FLOOR, ROUND, RAND, COUNT, DISTINCT, MIN, MAX, SUM,
         * AVG
         */
        @Test
        public void testScanTokens7() {
                Lexer lexer = new Lexer(
                                "SELECT MOD(10, 3), SHOW employees, DIV(10, 3), UCASE(\"hello\"), FLOOR(10.5), ROUND(10.5), RAND(), COUNT(*), COUNT(DISTINCT id), MIN(salary), MAX(salary), SUM(salary), AVG(salary) FROM employees;");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SELECT, "SELECT", "SELECT", 1));
                expected.add(
                                new Token(MOD, "MOD", "MOD", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(NUMBER, "10", 10.0, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(NUMBER, "3", 3.0, 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(SHOW, "SHOW", "SHOW", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(DIV, "DIV", "DIV", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(NUMBER, "10", 10.0, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(NUMBER, "3", 3.0, 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(UCASE, "UCASE", "UCASE", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(STRING, "\"hello\"", "hello", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(FLOOR, "FLOOR", "FLOOR", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(NUMBER, "10.5", 10.5, 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(ROUND, "ROUND", "ROUND", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(NUMBER, "10.5", 10.5, 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(RAND, "RAND", "RAND", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(COUNT, "COUNT", "COUNT", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(STAR, "*", null, 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(COUNT, "COUNT", "COUNT", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(DISTINCT, "DISTINCT", "DISTINCT", 1));
                expected.add(
                                new Token(IDENTIFIER, "ID", "ID", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(MIN, "MIN", "MIN", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(MAX, "MAX", "MAX", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(SUM, "SUM", "SUM", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(COMMA, ",", null, 1));
                expected.add(
                                new Token(AVG, "AVG", "AVG", 1));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

        /**
         * Testing the PIPE_PIPE and the IS
         */
        @Test
        public void testScanTokens8() {
                Lexer lexer = new Lexer(
                                "SELECT * FROM employees WHERE salary > 50000 || department_name IS NULL;");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SELECT, "SELECT", "SELECT", 1));
                expected.add(
                                new Token(STAR, "*", null, 1));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1));
                expected.add(
                                new Token(GREATER, ">", null, 1));
                expected.add(
                                new Token(NUMBER, "50000", 50000.0, 1));
                expected.add(
                                new Token(PIPE_PIPE, "||", null, 1));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1));
                expected.add(
                                new Token(IS, "IS", "IS", 1));
                expected.add(
                                new Token(NULL, "NULL", "NULL", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

        /**
         * Testing show tables
         */
        @Test
        public void testScanTokens9() {
                Lexer lexer = new Lexer(
                                "SHOW TABLES;");
                List<Token> tokens = lexer.scanTokens();

                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SHOW, "SHOW", "SHOW", 1));
                expected.add(
                                new Token(TABLES, "TABLES", "TABLES", 1));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1));
                expected.add(
                                new Token(EOF, "", null, 1));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }
}
