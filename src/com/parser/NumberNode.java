package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import java.util.ArrayList;


public class NumberNode implements JottTree {
    private final int number;

    public NumberNode(int number) {
        this.number = number;   
    }

    public static NumberNode parseNumberNode(ArrayList<Token> tokens) {
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
        return new NumberNode(Integer.parseInt(t.getToken()));
    }

    public String convertToJott() {
        // returns a string representation of the number
        return Integer.toString(number);
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
