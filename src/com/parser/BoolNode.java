package parser;

import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public class BoolNode implements ExpressionNode {
    
    private final Token boolToken;

    public BoolNode(Token boolToken) {
        this.boolToken = boolToken;
    }

    public static BoolNode parseBoolNode(ArrayList<Token> tokens) {
        Token t = tokens.remove(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD && (t.getToken().equals("true") || t.getToken().equals("false"))) {
                return new BoolNode(t);
            }
        return null;
    }
    
    @Override
    public String convertToJott() {
        return this.boolToken.getToken();
    }

    @Override
    public String convertToJava(String indentLevel) {
        return "";
    }

    @Override
    public boolean validateTree() {
        return true;
    }


    @Override
    public String convertToC() {
        return "";
    }

    @Override
    public String convertToPython() {
        return "";
    }
}
