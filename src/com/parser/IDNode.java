package parser;

import provided.*;
import java.util.ArrayList;

public class IDNode implements OperandNode {
    private final Token idToken;

    public IDNode(Token idToken) {
        this.idToken = idToken;
    }

    public static IDNode parseIDNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) throw new ParseException("Unexpected EOF", null);
		
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD) throw new ParseException("Missing an ID Keyword", t);
        
        // consume the token and return a new IDNode
        tokens.remove(0);
        return new IDNode(t);
    }

    public String convertToJott() {
        // returns a string representation of the token/ID
        return idToken.getToken();
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
