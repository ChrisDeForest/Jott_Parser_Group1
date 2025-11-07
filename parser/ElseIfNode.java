package parser;
import java.util.ArrayList;

import provided.*;
import semantics.*;

public class ElseIfNode implements JottTree {
    private final Token elseIfToken;
    private final ExpressionNode condition;
    private final BodyNode body;

    public ElseIfNode(Token elseIftoken, ExpressionNode condition, BodyNode body) {
        this.elseIfToken = elseIftoken;
        this.condition = condition;
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
        return "Elseif [" + this.condition.convertToJott() + "]{" + this.body.convertToJott() + "}";
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
	condition.validateTree(); // throws error if false

	String type = condition.getType(SymbolTable.globalSymbolTable);
	if (!type.equals("Boolean")) {
		throw new SemanticException("ElseIfNode: Elseif condition must be of type Boolean, but got '" + type + "'.", elseIfToken);
	}

	// Validate body with special marker to skip return validation
	// Returns inside elseif statements don't count as guaranteed returns for the function
	body.validateTree("__IF_STATEMENT_BODY__");
        return true;
    }
    
}
