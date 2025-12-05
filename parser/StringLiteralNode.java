package parser;

import java.util.ArrayList;

import provided.Token;
import semantics.SymbolTable;

public class StringLiteralNode implements ExpressionNode {

    private final Token stringLiteralToken;

    public StringLiteralNode(Token stringLiteralToken) {
        this.stringLiteralToken = stringLiteralToken;
    }

    public static StringLiteralNode parseStringLiteralNode(ArrayList<Token> tokens) {
        Token t = tokens.remove(0);
        return new StringLiteralNode(t);
    }

    @Override
    public String getType(SymbolTable symbolTable) {
        return "String";
    }

    @Override
    public String convertToJott() {
        return this.stringLiteralToken.getToken();
    }

    @Override
    public String convertToJava(String indentLevel) {
        return "";
    }

    @Override
    public boolean validateTree() {
        return true; // String literals are always valid
    }


    @Override
    public String convertToC() {
        return "";
    }

    @Override
    public String convertToPython() {
        return "";
    }

    @Override
    public Object evaluate() {
        return stringLiteralToken;
    }
}
