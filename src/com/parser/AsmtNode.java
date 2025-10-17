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
            System.err.println("parseAsmtNode: expected id but no tokens available");
            return null;
        }
		IDNode id = IDNode.parseIDNode(tokens);
		if (id == null) {
			System.err.println("parseAsmtNode: failed to parse id");
			return null;
		}

		// parse assignment section
		if (tokens.isEmpty()) {
			System.err.println("parseAsmtNode: expected '=' but no tokens available");
			return null;
		}
		Token assignToken = tokens.get(0);
		if (!assignToken.getTokenType().equals(TokenType.ASSIGN)) {
			System.err.println("parseAsmtNode: expected '=', but got '" + assignToken.getToken() + "' at " + assignToken.getFilename() + ":" + assignToken.getLineNum());
			return null;
		}
		tokens.remove(0);

		// parse expression section
		if (tokens.isEmpty()) {		
			System.err.println("parseAsmtNode: expected expression but no tokens available");
			return null;
		}
		ExpressionNode expr = ExpressionNode.parseExpressionNode(tokens);
		if (expr == null) {
			System.err.println("parseAsmtNode: failed to parse expression");
			return null;
		}

		// parse semicolon section
		if (tokens.isEmpty()) {
			System.err.println("parseAsmtNode: expected ';' but no tokens available");
			return null;
		}
		Token semiToken = tokens.get(0);
		if (!semiToken.getTokenType().equals(TokenType.SEMICOLON)) {
			System.err.println("parseAsmtNode: expected ';', but got '" + semiToken.getToken() + "' at " + semiToken.getFilename() + ":" + semiToken.getLineNum());
			return null;
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
