package parser;
import provided.*;
import semantics.*;

import java.util.ArrayList;

public class WhileLoopNode implements JottTree{
	private final Token whileToken;  // I actually dont know why we keep track of this but prof said so
	private final ExpressionNode condition;
    private final BodyNode body;

    public WhileLoopNode(Token token, ExpressionNode condition, BodyNode body) {
		this.whileToken = token;
		this.condition = condition;
		this.body = body;
    }

    public static WhileLoopNode parseWhileLoopNode(ArrayList<Token> tokens) {
		// < while_loop > -> While [ < expr >]{ < body >}

		// Check for while keyword
		if (tokens.isEmpty()){
			throw new ParseException("parseWhileLoopNode: Unexpected EOF", null);
		}
		if (!tokens.get(0).getToken().equals("While")) {
			throw new ParseException("parseWhileLoopNode: Expected 'While' Keyword", tokens.get(0));
		}
		Token whileToken = tokens.remove(0); // consume While


		// Check for opening bracket ([)
		if (tokens.isEmpty()){
			throw new ParseException("parseWhileLoopNode: Unexpected EOF", null);
		}
		if (!(tokens.get(0).getTokenType() == TokenType.L_BRACKET)) {
			throw new ParseException("parseWhileLoopNode: Expected '[' after 'While'", tokens.get(0));
		}
		tokens.remove(0); // consume ([)

		ExpressionNode condition = ExpressionNode.parseExpressionNode(tokens);

		// Check for closing bracket (])
		if (tokens.isEmpty()){
			throw new ParseException("parseWhileLoopNode: Unexpected EOF", null);
		}
		if (!(tokens.get(0).getTokenType() == TokenType.R_BRACKET)) {
			throw new ParseException("parseWhileLoopNode: Expected ']' to end condition", tokens.get(0));
		}
		tokens.remove(0); // consume (])
		
		// Check for opening brace ({)
		if (tokens.isEmpty()){
			throw new ParseException("parseWhileLoopNode: Unexpected EOF", null);
		}
		if (!(tokens.get(0).getTokenType() == TokenType.L_BRACE)) {
			throw new ParseException("parseWhileLoopNode: Expected { but got <id/keyword>", tokens.get(0));
		}
		tokens.remove(0); // consume ({)

		BodyNode body = BodyNode.parseBodyNode(tokens);

		// Check for closing brace (})
		if (tokens.isEmpty()){
			throw new ParseException("parseWhileLoopNode: Unexpected EOF", null);
		}
		if (!(tokens.get(0).getTokenType() == TokenType.R_BRACE)) {
			throw new ParseException("parseWhileLoopNode: Expected '}' to end while body", tokens.get(0));
		}
		tokens.remove(0); // consume ({)
		
        return new WhileLoopNode(whileToken, condition, body);
    }

	
	@Override
	public String convertToJott() {
		// < while_loop > -> While [ < expr >]{ < body >}
		return "While [" + this.condition.convertToJott() + "]{" + this.body.convertToJott() + "}";
	}

	@Override
	public String convertToJava(String indentLevel) {
		// TODO: Implement conversion logic
		return "";
	}

	@Override
	public boolean validateTree() {
		condition.validateTree(); // throws error if false

	
		// check if condition is a boolean
		String type = condition.getType(SymbolTable.globalSymbolTable);
		if (!type.equals("Boolean")) {
			throw new SemanticException("WhileLoopNode: While loop condition must be of type Boolean, but got '" + type + "'.", whileToken);
		}
		
		// Validate body with a special marker to skip return statement validation
		// Returns inside while loops don't count as guaranteed returns for the function
		body.validateTree("__WHILE_LOOP_BODY__");
		return true;
	}

	@Override
	public String convertToC() {
		// TODO: Implement conversion logic
		return "";
	}

	@Override
	public String convertToPython() {
		// TODO: Implement conversion logic
		return "";
	}

	@Override
	public Object execute() {
		while (true) {
			Object condVal = condition.execute();

			if (!((Boolean) condVal)) {
				break; // exit the loop
			}

			// exec body
			Object result = body.execute();

			// If body has return statement, return upward
			if (result != null) {
				return result;
			}
		}
    	// loop completed without a return
    	return null;
	}

}
