package edu.upvictoria.fpoo.XML;


public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;
    public final int start;
    public final int end;

    public Token(TokenType type, String lexeme, Object literal, int line, int start, int end) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return "Type: " + type + 
               "\nLexeme: " + lexeme + 
               "\nLiteral: " + literal + 
               "\nLine: " + line + 
               "\nStart: " + start + 
               "\nEnd: " + end;
    }

}

