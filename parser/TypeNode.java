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
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF while parsing <type>", null);
        }
        
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD) {
            throw new ParseException("Expected type keyword, got '" + t.getToken() + "'", t);
        }

        // Check if the token is a valid type: Double, Integer, String, or Boolean
        String tokenValue = t.getToken();
        if (!tokenValue.equals("Double") && !tokenValue.equals("Integer") && 
            !tokenValue.equals("String") && !tokenValue.equals("Boolean")) {
            throw new ParseException("Expected type (Double, Integer, String, or Boolean), got '" + tokenValue + "'", t);
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
