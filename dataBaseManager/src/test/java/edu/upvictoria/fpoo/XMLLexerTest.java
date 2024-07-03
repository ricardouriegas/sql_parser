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

}
