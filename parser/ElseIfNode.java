package parser;

import java.util.ArrayList;

import provided.*;

public class ElseIfNode implements JottTree {
	private final ExpressionNode condition;
	private final BodyNode body;

	public ElseIfNode(ExpressionNode condition, BodyNode body) {
		this.condition = condition;
		this.body = body;
	}

	public static ElseIfNode parseElseIfNode(ArrayList<Token> tokens) {
		// Elseif [ <expr> ] { <body> }
		if (tokens.isEmpty()) {
			throw new ParseException("parseElseIfNode: Unexpected EOF while parsing <elseif>", null);
		}
		Token kw = tokens.get(0);
		if (kw.getTokenType() != TokenType.ID_KEYWORD || !kw.getToken().equals("Elseif")) {
			throw new ParseException("parseElseIfNode: Expected 'Elseif', got '" + kw.getToken() + "'", kw);
		}
		tokens.remove(0);

		// [
		if (tokens.isEmpty())
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		Token lb = tokens.get(0);
		if (lb.getTokenType() != TokenType.L_BRACKET) {
			throw new ParseException("parseElseIfNode: Expected '[' after Elseif", lb);
		}
		tokens.remove(0);

		// <expr>
		ExpressionNode cond = ExpressionNode.parseExpressionNode(tokens);

		// ]
		if (tokens.isEmpty())
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		Token rb = tokens.get(0);
		if (rb.getTokenType() != TokenType.R_BRACKET) {
			throw new ParseException("parseElseIfNode: Expected ']' after Elseif condition", rb);
		}
		tokens.remove(0);

		// {
		if (tokens.isEmpty())
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		Token lbrace = tokens.get(0);
		if (lbrace.getTokenType() != TokenType.L_BRACE) {
			throw new ParseException("parseElseIfNode: Expected '{' after Elseif condition", lbrace);
		}
		tokens.remove(0);

		// <body>
		BodyNode body = BodyNode.parseBodyNode(tokens);

		// }
		if (tokens.isEmpty())
			throw new ParseException("parseElseIfNode: Unexpected EOF", null);
		Token rbrace = tokens.get(0);
		if (rbrace.getTokenType() != TokenType.R_BRACE) {
			throw new ParseException("parseElseIfNode: Expected '}' after Elseif body", rbrace);
		}
		tokens.remove(0);

		return new ElseIfNode(cond, body);
	}

	public ExpressionNode getCondition(){
		return condition;
	}

	@Override
	public String convertToJott() {
		StringBuilder sb = new StringBuilder();
		sb.append("Elseif[");
		sb.append(condition.convertToJott());
		sb.append("]{");
		sb.append(body.convertToJott());
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String convertToJava(String className) {
		return "";
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
	public boolean validateTree() {
		// condition must be Boolean
		condition.validateTree();
		String t = condition.getType(semantics.SymbolTable.globalSymbolTable);
		if (!"Boolean".equals(t)) {
			throw new semantics.SemanticException(
					"ElseIfNode: Elseif condition must be of type Boolean, but got '" + t + "'.", null);
		}
		// body validated as "if-body" (skip function-level return enforcement inside)
		body.validateTree("__IF_STATEMENT_BODY__");
		return true;
	}

	public BodyNode getBody() {
		return body;
	}

	public Object execute() {
		return null;
	}
}
