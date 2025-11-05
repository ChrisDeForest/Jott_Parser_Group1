package parser;

import java.util.ArrayList;

import provided.Token;
import provided.TokenType;
import semantics.SymbolTable;

public class BoolNode implements ExpressionNode {
    
    private final Token boolToken;

    public BoolNode(Token boolToken) {
        this.boolToken = boolToken;
    }

    public static BoolNode parseBoolNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new ParseException("parseBoolNode: Unexpected EOF while parsing boolean", null);
        }
        Token t = tokens.remove(0);
        if (t.getTokenType() == TokenType.ID_KEYWORD && (t.getToken().equals("True") || t.getToken().equals("False"))) {
            return new BoolNode(t);
        }
        throw new ParseException("parseBoolNode: Expected boolean value (true or false), got '" + t.getToken() + "'", t);
    }
    
    @Override
    public String convertToJott() {
        return this.boolToken.getToken();
    }

    @Override
    public String getType(SymbolTable symbolTable) {
        return "Boolean";
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
