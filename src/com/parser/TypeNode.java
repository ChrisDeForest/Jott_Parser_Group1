package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;
import java.util.ArrayList;

public class TypeNode implements JottTree {
    private final Token typeToken;

    public TypeNode(Token typeToken) {
        this.typeToken = typeToken;
    }

    public static TypeNode parseTypeNode(ArrayList<Token> tokens) {
        if (tokens == null || tokens.size() == 0) {
            System.err.println("parseTypeNode: expected type but no tokens available");
            return null;
        }
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD) {
            System.err.println("parseTypeNode: expected type keyword, got '" + t.getToken() + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }

        // Check if the token is a valid type: Double, Integer, String, or Boolean
        String tokenValue = t.getToken();
        if (!tokenValue.equals("Double") && !tokenValue.equals("Integer") && 
            !tokenValue.equals("String") && !tokenValue.equals("Boolean")) {
            System.err.println("parseTypeNode: expected type (Double, Integer, String, or Boolean), got '" + tokenValue + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }

        // consume the token and return a new TypeNode
        tokens.remove(0);
        return new TypeNode(t);
    }

    @Override
    public String convertToJott() {
        // returns a string representation of the type token
        return typeToken.getToken();
    }

    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
}
