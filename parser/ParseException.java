package parser;

import provided.Token;

public class ParseException extends RuntimeException {

    public ParseException(String message, Token token) {
        // For any syntax error
        super(buildMessage(message, token));
    }

    private static String buildMessage(String message, Token token) {
        // builds Error message
        if (token == null) { // When reaching EOF unexpectedly, empty token list
            return "Syntax Error\n" +
                    message;
        } else {
            return "Syntax Error\n" +
               message + "\n" +
               token.getFilename() + ":" + token.getLineNum();
        }
    }
}