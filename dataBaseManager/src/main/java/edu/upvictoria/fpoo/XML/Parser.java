package edu.upvictoria.fpoo.XML;

import java.util.*;

import static edu.upvictoria.fpoo.XML.TokenType.*;

/**
 * A parser really has two jobs:
 * 
 * 1. Given a valid sequence of tokens, produce a corresponding syntax tree. 
 * (in this case is not a syntax tree, is an XML tree)
 * 
 * 2. Given an invalid sequence of tokens, detect any errors and tell the user
 * about their mistakes.
 */
public class Parser {

    static class ParseError extends RuntimeException {
        // This is a simple sentinel class used to unwind the parser
    }

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // The parser
    // public List<XMLTree> parse() {
    //     try {
    //         return document();
    //     } catch (ParseError error) {
    //         // The parser promises not to crash or hang on invalid syntax,
    //         // but it doesn’t promise to return a usable syntax tree
    //         // if an error is found of course
    //         synchronize();
    //         return null;
    //     }
    // }


    /**
     * ************************************************************
     * Down here are the functions that are utilities
     * ************************************************************
     */
    /**
     * This function just consumes the next token if it is of the expected type
     * 
     * @param type
     * @param message
     * @return
     */
    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();

        throw error(peek(), message);
    }

    /**
     * This function is used to report an error and return a ParseError exception
     * 
     * @param token
     * @param message
     * @return
     */
    private ParseError error(Token token, String message) {
        ErrorHandler.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            // if (previous().type == SEMICOLON)
            //     return;

            // switch (peek().type) {
            //     case CREATE:
            //     case DROP:
            //     case SELECT:
            //     case INSERT:
            //     case UPDATE:
            //     case DELETE:
            //     case SHOW:
            //         return;
            // }

            advance();
        }
    }

    /**
     * This function is used to synchronize the parser after an error
     * with this i mean that it will iterate tokens until it finds a statement
     * 
     * @param types
     * @return boolean
     */
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    
}
