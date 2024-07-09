package edu.upvictoria.fpoo.SQL;

/**
 * This class will handle the errors on the libary
 */
public class ErrorHandler {

    /**
     * This one is for the lexer
     * 
     * @param line
     * @param message
     */
    public static void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * This one is for the parser
     * @param token
     * @param message
     */
    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    /**
     * This one is for the interpreter
     * @param message
     */
    public static void error(String message) {
        throw new Error(message);
    }

    /**
     * This method will report an error
     * 
     * @param line
     * @param where
     * @param message
     */
    private static void report(int line, String where, String message) {
        // hadError = true;
        throw new Error("[line " + line + "] Error" + where + ": " + message);
        // System.err.println(
        // "[line " + line + "] Error" + where + ": " + message);
    }
}
