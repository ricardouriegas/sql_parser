package edu.upvictoria.fpoo;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static edu.upvictoria.fpoo.TokenType.*;

public class LexerTest {
        /**
         * Test basic SELECT with \n
         */
        @Test
        public void testScanTokens0() {
                Lexer lexer = new Lexer("SELECT * \nfrom table;");
                List<Token> tokens = lexer.scanTokens();
                // System.out.println(tokens);
                // Expected:
                // SELECT SELECT null, STAR * null, FROM FROM null, TABLE TABLE null, SEMICOLON ; null, EOF
                // null
                List<Token> expected = new ArrayList<>();
                expected.add(
                                new Token(SELECT, "SELECT", "SELECT", 1, 0, 6));
                expected.add(
                                new Token(STAR, "*", '*', 1, 7, 8));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 2, 0, 4));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 2, 5, 10));
                expected.add(
                                new Token(SEMICOLON, ";", null, 2, 10, 11));
                expected.add(
                                new Token(EOF, "", null, 2, 11, 11));

                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }

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
                                new Token(SELECT, "SELECT", "SELECT", 1, 0, 6));
                expected.add(
                                new Token(STAR, "*", '*', 1, 7, 8));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1, 9, 13));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 1, 14, 19));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 20, 21));
                expected.add(
                                new Token(EOF, "", null, 1, 21, 21));

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
                                "CREATE TABLE Alumnos (id NUMBER NOT NULL PRIMARY KEY, nombre STRING NOT NULL, app STRING NOT NULL, apm STRING NOT NULL, edad INT);");
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
                                new Token(CREATE, "CREATE", "CREATE", 1, 0, 6));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 1, 7, 12));
                expected.add(
                                new Token(IDENTIFIER, "ALUMNOS", "ALUMNOS", 1, 13, 20));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 21, 22));
                expected.add(
                                new Token(IDENTIFIER, "ID", "ID", 1, 23, 25));
                expected.add(
                                new Token(NUMBER_DATA_TYPE, "NUMBER", "NUMBER", 1, 26, 32));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1, 33, 36));
                expected.add(
                                new Token(NULL, null, null, 1, 37, 41));
                expected.add(
                                new Token(PRIMARY, "PRIMARY", "PRIMARY", 1, 42, 49));
                expected.add(
                                new Token(KEY, "KEY", "KEY", 1, 50, 53));
                expected.add(
                                new Token(COMMA, ",", null, 1, 53, 54));
                expected.add(
                                new Token(IDENTIFIER, "NOMBRE", "NOMBRE", 1, 55, 61));
                expected.add(
                                new Token(STRING_DATA_TYPE, "STRING", "STRING", 1, 62, 68));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1, 69, 72));
                expected.add(
                                new Token(NULL, null, null, 1, 73, 77));
                expected.add(
                                new Token(COMMA, ",", null, 1, 77, 78));
                expected.add(
                                new Token(IDENTIFIER, "APP", "APP", 1, 79, 82));
                expected.add(
                                new Token(STRING_DATA_TYPE, "STRING", "STRING", 1, 83, 89));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1, 90, 93));
                expected.add(
                                new Token(NULL, null, null, 1, 94, 98));
                expected.add(
                                new Token(COMMA, ",", null, 1, 98, 99));
                expected.add(
                                new Token(IDENTIFIER, "APM", "APM", 1, 100, 103));
                expected.add(
                                new Token(STRING_DATA_TYPE, "STRING", "STRING", 1, 104, 110));
                expected.add(
                                new Token(NOT, "NOT", "NOT", 1, 111, 114));
                expected.add(
                                new Token(NULL, null, null, 1, 115, 119));
                expected.add(
                                new Token(COMMA, ",", null, 1, 119, 120));
                expected.add(
                                new Token(IDENTIFIER, "EDAD", "EDAD", 1, 121, 125));
                expected.add(
                                new Token(IDENTIFIER, "INT", "INT", 1, 126, 129));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 129, 130));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 130, 131));
                expected.add(
                                new Token(EOF, "", null, 1, 131, 131));

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
                                new Token(SELECT, "SELECT", "SELECT", 1, 0, 6));
                expected.add(
                                new Token(STAR, "*", '*', 1, 7, 8));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1, 9, 13));
                expected.add(
                                new Token(TABLE, "TABLE", "TABLE", 1, 14, 19));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1, 20, 25));
                expected.add(
                                new Token(IDENTIFIER, "ID", "ID", 1, 26, 28));
                expected.add(
                                new Token(EQUAL, "=", null, 1, 29, 30));
                expected.add(
                                new Token(NUMBER, "1", 1.0, 1, 31, 32));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 32, 33));
                expected.add(
                                new Token(EOF, "", null, 1, 33, 33));

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
                                new Token(UPDATE, "UPDATE", "UPDATE", 1, 0, 6));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1, 7, 15));
                expected.add(
                                new Token(SET, "SET", "SET", 1, 16, 19));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1, 20, 26));
                expected.add(
                                new Token(EQUAL, "=", null, 1, 27, 28));
                expected.add(
                                new Token(NUMBER, "60000", 60000.0, 1, 29, 34));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1, 35, 40));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1, 41, 56));
                expected.add(
                                new Token(EQUAL, "=", null, 1, 57, 58));
                expected.add(
                                new Token(STRING, "\"Engineering\"", "Engineering", 1, 59, 71));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 71, 72));
                expected.add(
                                new Token(EOF, "", null, 1, 72, 72));

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
                                new Token(DELETE, "DELETE", "DELETE", 1, 0, 6));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1, 7, 11));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1, 12, 20));
                expected.add(
                                new Token(WHERE, "WHERE", "WHERE", 1, 21, 26));
                expected.add(
                                new Token(IDENTIFIER, "DEPARTMENT_NAME", "DEPARTMENT_NAME", 1, 27, 42));
                expected.add(
                                new Token(EQUAL, "=", null, 1, 43, 44));
                expected.add(
                                new Token(STRING, "\"Engineering\"", "Engineering", 1, 45, 57));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 57, 58));
                expected.add(
                                new Token(EOF, "", null, 1, 58, 58));

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
                                new Token(SELECT, "SELECT", "SELECT", 1, 0, 6));
                expected.add(
                                new Token(MOD, "MOD", "MOD", 1, 7, 10));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 10, 11));
                expected.add(
                                new Token(NUMBER, "10", 10.0, 1, 11, 13));
                expected.add(
                                new Token(COMMA, ",", null, 1, 13, 14));
                expected.add(
                                new Token(NUMBER, "3", 3.0, 1, 15, 16));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 16, 17));
                expected.add(
                                new Token(COMMA, ",", null, 1, 17, 18));
                expected.add(
                                new Token(SHOW, "SHOW", "SHOW", 1, 19, 23));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1, 24, 32));
                expected.add(
                                new Token(COMMA, ",", null, 1, 32, 33));
                expected.add(
                                new Token(DIV, "DIV", "DIV", 1, 34, 37));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 37, 38));
                expected.add(
                                new Token(NUMBER, "10", 10.0, 1, 38, 40));
                expected.add(
                                new Token(COMMA, ",", null, 1, 40, 41));
                expected.add(
                                new Token(NUMBER, "3", 3.0, 1, 42, 43));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 43, 44));
                expected.add(
                                new Token(COMMA, ",", null, 1, 44, 45));
                expected.add(
                                new Token(UCASE, "UCASE", "UCASE", 1, 46, 51));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 51, 52));
                expected.add(
                                new Token(STRING, "\"hello\"", "hello", 1, 52, 59));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 59, 60));
                expected.add(
                                new Token(COMMA, ",", null, 1, 60, 61));
                expected.add(
                                new Token(FLOOR, "FLOOR", "FLOOR", 1, 62, 67));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 67, 68));
                expected.add(
                                new Token(NUMBER, "10.5", 10.5, 1, 68, 72));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 72, 73));
                expected.add(
                                new Token(COMMA, ",", null, 1, 73, 74));
                expected.add(
                                new Token(ROUND, "ROUND", "ROUND", 1, 75, 80));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 80, 81));
                expected.add(
                                new Token(NUMBER, "10.5", 10.5, 1, 81, 85));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 85, 86));
                expected.add(
                                new Token(COMMA, ",", null, 1, 86, 87));
                expected.add(
                                new Token(RAND, "RAND", "RAND", 1, 88, 92));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 92, 93));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 93, 94));
                expected.add(
                                new Token(COMMA, ",", null, 1, 94, 95));
                expected.add(
                                new Token(COUNT, "COUNT", "COUNT", 1, 96, 101));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 101, 102));
                expected.add(
                                new Token(STAR, "*", '*', 1, 102, 103));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 103, 104));
                expected.add(
                                new Token(COMMA, ",", null, 1, 104, 105));
                expected.add(
                                new Token(COUNT, "COUNT", "COUNT", 1, 106, 111));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 111, 112));
                expected.add(
                                new Token(DISTINCT, "DISTINCT", "DISTINCT", 1, 112, 120));
                expected.add(
                                new Token(IDENTIFIER, "ID", "ID", 1, 121, 123));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 123, 124));
                expected.add(
                                new Token(COMMA, ",", null, 1, 124, 125));
                expected.add(
                                new Token(MIN, "MIN", "MIN", 1, 126, 129));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 129, 130));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1, 130, 136));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 136, 137));
                expected.add(
                                new Token(COMMA, ",", null, 1, 137, 138));
                expected.add(
                                new Token(MAX, "MAX", "MAX", 1, 139, 142));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 142, 143));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1, 143, 149));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 149, 150));
                expected.add(
                                new Token(COMMA, ",", null, 1, 150, 151));
                expected.add(
                                new Token(SUM, "SUM", "SUM", 1, 152, 155));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 155, 156));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1, 156, 162));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 162, 163));
                expected.add(
                                new Token(COMMA, ",", null, 1, 163, 164));
                expected.add(
                                new Token(AVG, "AVG", "AVG", 1, 165, 168));
                expected.add(
                                new Token(LEFT_PAREN, "(", null, 1, 168, 169));
                expected.add(
                                new Token(IDENTIFIER, "SALARY", "SALARY", 1, 169, 175));
                expected.add(
                                new Token(RIGHT_PAREN, ")", null, 1, 175, 176));
                expected.add(
                                new Token(FROM, "FROM", "FROM", 1, 177, 181));
                expected.add(
                                new Token(IDENTIFIER, "EMPLOYEES", "EMPLOYEES", 1, 182, 190));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 190, 191));
                expected.add(
                                new Token(EOF, "", null, 1, 191, 191));

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
                                new Token(SHOW, "SHOW", "SHOW", 1, 0, 4));
                expected.add(
                                new Token(TABLES, "TABLES", "TABLES", 1, 5, 11));
                expected.add(
                                new Token(SEMICOLON, ";", null, 1, 11, 12));
                expected.add(
                                new Token(EOF, "", null, 1, 12, 12));

                // Assertion
                for (int i = 0; i < expected.size(); i++) {
                        assertEquals(expected.get(i).type, tokens.get(i).type);
                        assertEquals(expected.get(i).lexeme, tokens.get(i).lexeme);
                        assertEquals(expected.get(i).literal, tokens.get(i).literal);
                        assertEquals(expected.get(i).line, tokens.get(i).line);
                }
        }
}
