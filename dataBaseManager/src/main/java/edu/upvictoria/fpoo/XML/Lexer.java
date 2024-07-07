package edu.upvictoria.fpoo.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upvictoria.fpoo.SQL.ErrorHandler;

import static edu.upvictoria.fpoo.XML.TokenType.*;
import static edu.upvictoria.fpoo.SQL.ErrorHandler.*;

public class Lexer {
    // The query to be tokenized
    private final String query;
    
    // List of tokens on the query
    private final List<Token> tokens = new ArrayList<>();

    // Variables to keep track of the current lexeme being scanned
    private int start = 0;
    private int current = 0;
    private int line = 1;

    // Map of SQL keywords
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        
        // DTD keywords
        keywords.put("ELEMENT", ELEMENT);
        keywords.put("ATTLIST", ATTLIST);
        keywords.put("ENTITY", ENTITY);
        keywords.put("NOTATION", NOTATION);
        keywords.put("REQUIRED", REQUIRED);
        keywords.put("IMPLIED", IMPLIED);
        keywords.put("FIXED", FIXED);
        keywords.put("PUBLIC", PUBLIC);
        keywords.put("SYSTEM", SYSTEM);
        keywords.put("NDATA", NDATA);

        // XML keywords
        keywords.put("XML", XML);
        keywords.put("VERSION", VERSION);
        keywords.put("ENCODING", ENCODING);
        keywords.put("STANDALONE", STANDALONE);
        keywords.put("DOCTYPE", DOCTYPE);
        keywords.put("CDATA", CDATA);
        keywords.put("COMMENT", COMMENT);
        keywords.put("ENTITYREF", ENTITYREF);
        keywords.put("CHARREF", CHARREF);
        keywords.put("PI", PI);
        keywords.put("XMLNS", XMLNS);
         
    }

    /**
     * Constructor
     * @param query
     */
    public Lexer(String query) {
        this.query = query;
    }

    /**
     * This method will scan to get the next token
     * 
     * @return List<Token> (list of tokens)
     */
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;

            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line, current, current));
        return tokens;
    }

    /**
     * This method will scan the token
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '<':
                addToken(LEFT_ANGLE);
                break;
            case '>':
                addToken(RIGHT_ANGLE);
                break;
            case '/':
                addToken(SLASH);
                break;
            case '!':
                addToken(EXCLAMATION);
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                // move next line
                line++;
                break;
            case '"':
                string();
                break;
            case '\'':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    ErrorHandler.error(line, "Unexpected character: " + c);
                }
                break;
        }
    }

    /**
     * This method will scan the identifier
     */
    private void identifier() {
        // while the character is alphanumeric
        while (isAlphaNumeric(peek()))
            advance();

        // see if it matches anything in the map
        String text = query.substring(start, current);
        TokenType type = keywords.get(text.toUpperCase()); // see if it matches any keyword in uppercase
        
        if (type == null)  
            type = IDENTIFIER;

        addToken(type, text); 
    }

    
    /**
     * Just a method to check if the character is a letter
     * 
     * @param c
     * @return
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }
    
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
    /**
     * Just a method to check if the character is a digit
     * 
     * @param c
     * @return
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * This method will scan a number
     */
    private void number() {
        while (isDigit(peek()))
            advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek()))
                advance();
        }

        // convert the string to a double
        addToken(NUMBER,
                Double.parseDouble(query.substring(start, current)));
    }

    
    /**
     * string() method will scan the string
     */
    private void string() {
        while (peek() != '"' && peek() != '\'' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }
        
        if (isAtEnd()) {
            ErrorHandler.error(line, "Unterminated string.");
            return;
        }
        
        // The closing " or '.
        advance();
        
        // Trim the surrounding quotes.
        String value = query.substring(start + 1, current - 1);
        addToken(STRING, value);
    }
    
    /**
     * Method used to peek the next character (if there is one)
     * 
     * @return
     */
    private char peekNext() {
        if (current + 1 >= query.length())
            return '\0';
        return query.charAt(current + 1);
    }
    
    /**
     * Look ahead to see if the current character is a digit
     * 
     * @return
     */
    private char peek() {
        if (isAtEnd())
            return '\0';
        return query.charAt(current);
    }

    /**
     * match the digit
     * 
     * @param expected
     * @return
     */
    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (query.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    /**
     * Consumes the next character
     * 
     * @return
     */
    private char advance() {
        return query.charAt(current++);
    }

    /**
     * Grabs the currect lexeme and adds it to the tokens list
     * 
     * @param type
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Grabs the current lexeme and adds it to the tokens list
     * 
     * @param type
     * @param literal
     */
    private void addToken(TokenType type, Object literal) {
        String text = query.substring(start, current);

        tokens.add(new Token(type, text, literal, line, start, current));
    }

    /**
     * This method will return true if we are at the end of the query
     */
    private boolean isAtEnd() {
        return current >= query.length();
    }

}
