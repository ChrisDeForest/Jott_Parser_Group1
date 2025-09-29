package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import java.util.ArrayList;

public class IDNode implements JottTree {
    private final String id;

    public IDNode(String id) {
        this.id = id;
    }

    public static IDNode parseIDNode(ArrayList<Token> tokens) {
        if (tokens == null || tokens.size() == 0) {
            System.err.println("parseIDNode: expected identifier but no tokens available");
            return null;
        }
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD) {
            System.err.println("parseIDNode: expected ID token, got '" + t.getToken() + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }
        // TODO this is technically redundant thanks Carlo
        char firstChar = t.getToken().charAt(0);
        if (!Character.isLetter(firstChar)){    
            System.err.println("parseIDNode: expected letter, got '" + t.getToken() + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }

        // consume the token and return a new IDNode
        tokens.remove(0);
        return new IDNode(t.getToken());
    }

    public String convertToJott() {
        // returns a string representation of the token/ID
        return id;
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
