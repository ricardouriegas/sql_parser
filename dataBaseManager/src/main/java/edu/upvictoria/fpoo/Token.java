package edu.upvictoria.fpoo;


public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;
    final int[] range = new int[2];

    Token(TokenType type, String lexeme, Object literal, int line, int start, int end) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.range[0] = start;
        this.range[1] = end;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal + " " + line;
    }

}
