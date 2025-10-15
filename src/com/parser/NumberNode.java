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
        if (tokens == null || tokens.size() == 0) {
            System.err.println("parseNumberNode: expected number but no tokens available");
            return null;
        }
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.NUMBER) {
            System.err.println("parseNumberNode: expected NUMBER token, got '" + t.getToken() + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }
        char firstChar = t.getToken().charAt(0);
        if (!Character.isDigit(firstChar)){
            System.err.println("parseNumberNode: expected number, got '" + t.getToken() + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }
        // consume the token and return a new NumberNode
        tokens.remove(0);
        return new NumberNode(t, isNegative);
    }

    public String convertToJott() {
        // returns a string representation of the number
        if (isNegative) {
            return "-" + numberToken.getToken();
        }
        else {
            return numberToken.getToken();
        }
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
