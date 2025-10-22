package parser;
import java.util.ArrayList;

import provided.*;

public class ElseIfNode implements JottTree {
    private final Token elseIfToken;
    private final ExpressionNode expression;
    private final BodyNode body;

    public ElseIfNode(Token elseIftoken, ExpressionNode expression, BodyNode body) {
        this.elseIfToken = elseIftoken;
        this.expression = expression;
        this.body = body;
    }

    public static ElseIfNode parseElseIfNode(ArrayList<Token> tokens) {
        // Elseif [ < expr >]{ < body >}


       	if (tokens.isEmpty()){
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		}
		if (!tokens.get(0).getToken().equals("Elseif")) {
			throw new ParseException("parseElseIfNode: Missing 'Elseif' Keyword", tokens.get(0));
		}
		Token elseIfToken = tokens.remove(0); // consume Elseif


		// Check for opening bracket ([)
		if (tokens.isEmpty()){
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		}
		if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
			throw new ParseException("parseElseIfNode: Missing '[' after 'Elseif'", tokens.get(0));
		}
		tokens.remove(0); // consume ([)

		ExpressionNode condition = ExpressionNode.parseExpressionNode(tokens);

		// Check for closing bracket (])
		if (tokens.isEmpty()){
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		}
		if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
			throw new ParseException("parseElseIfNode: Missing ']' after Elseif condition", tokens.get(0));
		}
		tokens.remove(0); // consume (])
		
		// Check for opening brace ({)
		if (tokens.isEmpty()){
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		}
		if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
			throw new ParseException("parseElseIfNode: Missing '{' to start Elseif body", tokens.get(0));
		}
		tokens.remove(0); // consume ({)

		BodyNode body = BodyNode.parseBodyNode(tokens);

		// Check for closing brace (})
		if (tokens.isEmpty()){
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		}
		if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
			throw new ParseException("parseElseIfNode: Missing '{' to end Elseif body", tokens.get(0));
		}
		tokens.remove(0); // consume ({)

        return new ElseIfNode(elseIfToken, condition, body);
    }

    @Override
    public String convertToJott() {
        // Elseif [ < expr >]{ < body >}
        return "Elseif [" + this.expression.convertToJott() + "]{" + this.body.convertToJott() + "}";
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
