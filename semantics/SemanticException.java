package semantics;

import provided.Token;

public class SemanticException extends RuntimeException {

    public SemanticException(String message, Token token) {
        // For any semantic error
        super(buildMessage(message, token));
    }

    private static String buildMessage(String message, Token token) {
        // builds Error message
        if (token == null) { // When reaching EOF unexpectedly, empty token list
            return "Semantic Error\n" +
                    message;
        } else {
            return "Semantic Error\n" +
               message + "\n" +
               token.getFilename() + ":" + token.getLineNum();
        }
    }
}