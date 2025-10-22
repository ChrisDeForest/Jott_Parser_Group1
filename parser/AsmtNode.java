package parser;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class AsmtNode implements JottTree {

	private final IDNode id;
	private final ExpressionNode expr;

    public AsmtNode(IDNode id, ExpressionNode expr) {
		this.id = id;
		this.expr = expr;
    }

    public static AsmtNode parseAsmtNode(ArrayList<Token> tokens) {
		// parse id section
        if (tokens == null || tokens.isEmpty()) {
            throw new ParseException("parseAsmtNode: Expected id but no tokens available", null);
        }
		IDNode id = IDNode.parseIDNode(tokens);
		if (id == null) {
			throw new ParseException("parseAsmtNode: Failed to parse id", null);
		}

		// parse assignment section
		if (tokens.isEmpty()) {
			throw new ParseException("parseAsmtNode: Expected '=' but no tokens available", null);
		}
		Token assignToken = tokens.get(0);
		if (!assignToken.getTokenType().equals(TokenType.ASSIGN)) {
			throw new ParseException("parseAsmtNode: Expected '=', but got '" + assignToken.getToken() + "'", assignToken);
		}
		tokens.remove(0);

		// parse expression section
		if (tokens.isEmpty()) {		
			throw new ParseException("parseAsmtNode: Expected expression but no tokens available", null);
		}
		ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
		if (expr == null) {
			throw new ParseException("parseAsmtNode: Failed to parse expression", null);
		}

		// parse semicolon section
		if (tokens.isEmpty()) {
			throw new ParseException("parseAsmtNode: Expected ';' but no tokens available", null);
		}
		Token semiToken = tokens.get(0);
		if (!semiToken.getTokenType().equals(TokenType.SEMICOLON)) {
			throw new ParseException("parseAsmtNode: Expected ';', but got '" + semiToken.getToken() + "'", semiToken);
		}
		tokens.remove(0);

		// create AsmtNode
		return new AsmtNode(id, expr);	
    }

	@Override
	public String convertToJott() {
		StringBuilder sb = new StringBuilder();
		sb.append(id.convertToJott());
		sb.append(" = ");
		sb.append(expr.convertToJott());
		sb.append(";");
		return sb.toString();
	}
    
	@Override
	public String convertToJava(String className) {
		// TODO: Implement conversion logic
		return "";
	}

	@Override
	public boolean validateTree() {
		// TODO: Implement validation logic
		return false;
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
}
