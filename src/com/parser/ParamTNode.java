package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.ParseException;

import java.util.ArrayList;

public class ParamTNode implements JottTree {

    final JottTree expr; // ExpressionNode

    public ParamTNode(JottTree expr) {
        this.expr = expr;
    }

    // <params_t> -> , <expr>
    public static ParamTNode parseParamTNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty())
            throw new ParseException("Unexpected EOF in params tail", null);

        Token comma = tokens.get(0);
        if (comma.getTokenType() != TokenType.COMMA) {
            throw new ParseException("Expected ',' between function call arguments", comma);
        }
        tokens.remove(0);

        JottTree e = ExpressionNode.parseExpressionNode(tokens);
        return new ParamTNode(e);
    }

    @Override
    public String convertToJott() {
        return "," + expr.convertToJott();
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
        return true;
    }
}
