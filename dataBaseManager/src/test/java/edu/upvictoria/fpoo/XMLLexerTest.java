package edu.upvictoria.fpoo;

import java.util.*;

import org.junit.Test;

import edu.upvictoria.fpoo.XML.Lexer;
import edu.upvictoria.fpoo.XML.Token;

import static edu.upvictoria.fpoo.XML.TokenType.*;
import static org.junit.Assert.*;

public class XMLLexerTest {
    /**
     * Test basic XML
     */
    @Test
    public void testScanTokens0() {
        Lexer lexer = new Lexer("<foo></foo>");
        List<Token> tokens = lexer.scanTokens();

        assertEquals(8, tokens.size()); // 7 tokens + EOF
        assertEquals(LEFT_ANGLE, tokens.get(0).type);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals(RIGHT_ANGLE, tokens.get(2).type);
        assertEquals(LEFT_ANGLE, tokens.get(3).type);
        assertEquals(SLASH, tokens.get(4).type);

        assertEquals("<", tokens.get(0).lexeme);
        assertEquals("foo", tokens.get(1).lexeme);
        assertEquals(">", tokens.get(2).lexeme);
        assertEquals("<", tokens.get(3).lexeme);
        assertEquals("/", tokens.get(4).lexeme);
    }

    /**
     * Test basic XML
     */
    @Test
    public void testScanTokens1() {
        Lexer lexer = new Lexer("<foo><bar>hello</bar></foo>");
        List<Token> tokens = lexer.scanTokens();

        assertEquals(16, tokens.size()); // 15 tokens + EOF
        assertEquals(LEFT_ANGLE, tokens.get(0).type);
        assertEquals(IDENTIFIER, tokens.get(1).type);
        assertEquals(RIGHT_ANGLE, tokens.get(2).type);
        assertEquals(LEFT_ANGLE, tokens.get(3).type);
        assertEquals(IDENTIFIER, tokens.get(4).type);
        assertEquals(RIGHT_ANGLE, tokens.get(5).type);
        assertEquals(IDENTIFIER, tokens.get(6).type);
        assertEquals(LEFT_ANGLE, tokens.get(7).type);
        assertEquals(SLASH, tokens.get(8).type);
        assertEquals(IDENTIFIER, tokens.get(9).type);
        assertEquals(RIGHT_ANGLE, tokens.get(10).type);
        assertEquals(LEFT_ANGLE, tokens.get(11).type);
        assertEquals(SLASH, tokens.get(12).type);

        assertEquals("<", tokens.get(0).lexeme);
        assertEquals("foo", tokens.get(1).lexeme);
        assertEquals(">", tokens.get(2).lexeme);
        assertEquals("<", tokens.get(3).lexeme);
        assertEquals("bar", tokens.get(4).lexeme);
        assertEquals(">", tokens.get(5).lexeme);
        assertEquals("hello", tokens.get(6).lexeme);
        assertEquals("<", tokens.get(7).lexeme);
        assertEquals("/", tokens.get(8).lexeme);
        assertEquals("bar", tokens.get(9).lexeme);
        assertEquals(">", tokens.get(10).lexeme);
        assertEquals("<", tokens.get(11).lexeme);
        assertEquals("/", tokens.get(12).lexeme);
    }

    /**
     * Test DTD declaration
     */
    @Test
    public void testScanTokens2() {
        Lexer lexer = new Lexer("<!DOCTYPE foo SYSTEM \"foo.dtd\">");
        List<Token> tokens = lexer.scanTokens();

        assertEquals(8, tokens.size()); // 7 tokens + EOF
        assertEquals(LEFT_ANGLE, tokens.get(0).type);
        assertEquals(EXCLAMATION, tokens.get(1).type);
        assertEquals(DOCTYPE, tokens.get(2).type);
        assertEquals(IDENTIFIER, tokens.get(3).type);
        assertEquals(SYSTEM, tokens.get(4).type);
        assertEquals(STRING, tokens.get(5).type);
        assertEquals(RIGHT_ANGLE, tokens.get(6).type);

        assertEquals("<", tokens.get(0).lexeme);
        assertEquals("!", tokens.get(1).lexeme);
        assertEquals("DOCTYPE", tokens.get(2).lexeme);
        assertEquals("foo", tokens.get(3).lexeme);
        assertEquals("SYSTEM", tokens.get(4).lexeme);
        assertEquals("\"foo.dtd\"", tokens.get(5).lexeme);
        assertEquals(">", tokens.get(6).lexeme);
        
    }

    /**
     * Test DTD declaration with XML content
     */
    @Test
    public void testScanTokens3() {
        Lexer lexer = new Lexer("<!DOCTYPE foo SYSTEM \"foo.dtd\"><foo>hola</foo>");
        List<Token> tokens = lexer.scanTokens();

        assertEquals(16, tokens.size()); // 15 tokens + EOF
        assertEquals(LEFT_ANGLE, tokens.get(0).type);
        assertEquals(EXCLAMATION, tokens.get(1).type);
        assertEquals(DOCTYPE, tokens.get(2).type);
        assertEquals(IDENTIFIER, tokens.get(3).type);
        assertEquals(SYSTEM, tokens.get(4).type);
        assertEquals(STRING, tokens.get(5).type);
        assertEquals(RIGHT_ANGLE, tokens.get(6).type);
        assertEquals(LEFT_ANGLE, tokens.get(7).type);
        assertEquals(IDENTIFIER, tokens.get(8).type);
        assertEquals(RIGHT_ANGLE, tokens.get(9).type);
        assertEquals(IDENTIFIER, tokens.get(10).type);
        assertEquals(LEFT_ANGLE, tokens.get(11).type);
        assertEquals(SLASH, tokens.get(12).type);
        assertEquals(IDENTIFIER, tokens.get(13).type);
        assertEquals(RIGHT_ANGLE, tokens.get(14).type);

        assertEquals("<", tokens.get(0).lexeme);
        assertEquals("!", tokens.get(1).lexeme);
        assertEquals("DOCTYPE", tokens.get(2).lexeme);
        assertEquals("foo", tokens.get(3).lexeme);
        assertEquals("SYSTEM", tokens.get(4).lexeme);
        assertEquals("\"foo.dtd\"", tokens.get(5).lexeme);
        assertEquals(">", tokens.get(6).lexeme);
        assertEquals("<", tokens.get(7).lexeme);
        assertEquals("foo", tokens.get(8).lexeme);
        assertEquals(">", tokens.get(9).lexeme);
        assertEquals("hola", tokens.get(10).lexeme);
        assertEquals("<", tokens.get(11).lexeme);
        assertEquals("/", tokens.get(12).lexeme);
        assertEquals("foo", tokens.get(13).lexeme);
        assertEquals(">", tokens.get(14).lexeme);
    }

}
