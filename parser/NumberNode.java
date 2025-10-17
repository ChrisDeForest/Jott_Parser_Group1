package parser;

import provided.Token;
import provided.TokenType;
import java.util.ArrayList;


public class NumberNode implements OperandNode {
    private final Token numberToken;
    private final boolean isNegative;

    public NumberNode(Token token, boolean isNegative) {
        this.numberToken = token;   
        this.isNegative = isNegative;
    }

    public static NumberNode parseNumberNode(ArrayList<Token> tokens, boolean isNegative) {
        if (tokens == null || tokens.isEmpty()) throw new ParseException("parseNumberNode: expected number but no tokens available", null);
           
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.NUMBER) throw new ParseException("parseNumberNode: expected NUMBER token, got ", t);
            
        char firstChar = t.getToken().charAt(0);
        if (!Character.isDigit(firstChar)) throw new ParseException("parseNumberNode: expected number, got ", t);
            
        // consume the token and return a new NumberNode
        tokens.remove(0);
        return new NumberNode(t, isNegative);
    }

    public String convertToJott() {
        // returns a string representation of the number
        return isNegative ? "-" + numberToken.getToken() : numberToken.getToken();
    }

    public String convertToJava(String classname) {
        return null;
    }
    public String convertToC() {
        return null;
    }
    public String convertToPython() {
        return null;
    }
    public boolean validateTree() {
        return false;
    }
}
