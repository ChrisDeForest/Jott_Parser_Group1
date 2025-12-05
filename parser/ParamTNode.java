package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class ParamTNode implements JottTree {

    final ExpressionNode expr; // ExpressionNode

    public ParamTNode(ExpressionNode expr) {
        this.expr = expr;
    }

    // <params_t> -> , <expr>
    public static ParamTNode parseParamTNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty())
            throw new ParseException("parseParamTNode: Unexpected EOF in params tail", null);

        Token comma = tokens.get(0);
        if (comma.getTokenType() != TokenType.COMMA) {
            throw new ParseException("parseParamTNode: Expected ',' between function call arguments", comma);
        }
        tokens.remove(0);

        ExpressionNode e = ExpressionNode.parseExpressionNode(tokens);
        return new ParamTNode(e);
    }

    public ExpressionNode getExprNode() {
        return expr;
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
        // i dont think this actually ever gets called bc of how params is implemented
        expr.validateTree();
        return true;
    }

    @Override 
	public Object evaluate(){
		return null;
	}
}
