package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;
import semantics.SemanticException;
import semantics.SymbolTable;

import java.util.ArrayList;

public class ReturnStmtNode implements JottTree {
    private final Token returnToken;
    private final ExpressionNode expression;
    private final boolean isEmpty;

    public ReturnStmtNode(ExpressionNode expression, boolean isEmpty, Token returnToken) {
        this.returnToken = returnToken;
        this.expression = expression;
        this.isEmpty = isEmpty;
    }

    public static ReturnStmtNode parseReturnStmtNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) {
            // Even if there is no return statement, there should be more token(s) like }
            throw new ParseException("parseReturnStmtNode: missing closing }", null);
        }

        Token t = tokens.get(0);

        // Check if this is a Return statement (If there is no return statement, the
        // TokenType would be R_Bracket leading to epsilon)
        if (t.getTokenType() != TokenType.ID_KEYWORD || !t.getToken().equals("Return")) {
            // Epsilon case - no return statement
            return new ReturnStmtNode(null, true, t);
        }

        // Consume "Return" keyword
        tokens.remove(0);

        // Parse the expression (will throw ParseException if it fails)
        ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);

        // Check for semicolon
        if (tokens.isEmpty()) {
            throw new ParseException("parseReturnStmtNode: Unexpected EOF", null);
        }

        Token semicolon = tokens.get(0);
        if (semicolon.getTokenType() != TokenType.SEMICOLON) {
            throw new ParseException(
                    "parseReturnStmtNode: Expected ';' after return expression, got '" + semicolon.getToken() + "'",
                    semicolon);
        }

        // Consume semicolon
        tokens.remove(0);

        return new ReturnStmtNode(expr, false, t);
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
        return validateTree("Void");
    }

    // parser/ReturnStmtNode.java
    public boolean validateTree(String expectedReturnType) {
        String expectedType = expectedReturnType;

        if (isEmpty) {
            if ("Void".equals(expectedType)) {
                return true;
            }
            throw new SemanticException(
                    "ReturnStmtNode: Expected a return value of type '" + expectedType + "', but no value was returned.", null);
        }

        if (expression == null) {
            throw new SemanticException("ReturnStmtNode: Return statement without an expression.", returnToken);
        }

        String exprType = expression.getType(SymbolTable.globalSymbolTable);

        if ("Void".equals(expectedType)) {
            throw new SemanticException("ReturnStmtNode: Void functions must not return a value.", returnToken);
        }

        if (exprType == null) {
            throw new SemanticException(
                    "ReturnStmtNode: Unable to determine the type of the return expression.", returnToken);
        }

        if (!exprType.equals(expectedType)) {
            throw new SemanticException(
                    "ReturnStmtNode: Return type mismatch. Expected '" + expectedType + "', but found '" + exprType + "'.", returnToken);
        }

        return true;
    }

}
