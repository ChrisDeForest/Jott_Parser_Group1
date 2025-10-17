package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;

import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {
    private final ExpressionNode expression;
    private final boolean isEmpty;

    public ReturnStmtNode(ExpressionNode expression, boolean isEmpty) {
        this.expression = expression;
        this.isEmpty = isEmpty;
    }

    public static ReturnStmtNode parseReturnStmtNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) {
            // Even if there is no return statement, there should be more token(s) like }
            throw new ParseException("Unexpected EOF while parsing <return_stmt>", null);
        }

        Token t = tokens.get(0);
        
        // Check if this is a Return statement (If there is no return statement, the TokenType would be R_Bracket leading to epsilon)
        if (t.getTokenType() != TokenType.ID_KEYWORD || !t.getToken().equals("Return")) {
            // Epsilon case - no return statement
            return new ReturnStmtNode(null, true);
        }

        // Consume "Return" keyword
        tokens.remove(0);

        // Parse the expression (will throw ParseException if it fails)
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);

        // Check for semicolon
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF", null);
        }

        Token semicolon = tokens.get(0);
        if (semicolon.getTokenType() != TokenType.SEMICOLON) {
            throw new ParseException("Expected ';' after return expression, got '" + semicolon.getToken() + "'", semicolon);
        }

        // Consume semicolon
        tokens.remove(0);

        return new ReturnStmtNode(expr, false);
    }

    @Override
    public String convertToJott() {
        if (isEmpty) {
            return "";
        }
        return "Return " + expression.convertToJott() + ";";
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
