package parser;

import java.util.ArrayList;

import provided.*;

public class BodyNode implements JottTree {
	private final ArrayList<JottTree> bodyStmtNodes;
	private final ArrayList<Boolean> isFunctionCall;
	private final ReturnStmtNode returnStmt;

	public BodyNode(ArrayList<JottTree> bodyStmtNodes, ArrayList<Boolean> isFunctionCall, ReturnStmtNode returnStmt) {
		this.bodyStmtNodes = bodyStmtNodes;
		this.isFunctionCall = isFunctionCall;
		this.returnStmt = returnStmt;
	}

	public static BodyNode parseBodyNode(ArrayList<Token> tokens) {
		// < body > -> < body_stmt >⋆ < return_stmt >
		if (tokens.isEmpty()) {
			throw new ParseException("parseBodyNode: Unexpected EOF while parsing <body>", null);
		}

		ArrayList<JottTree> bodyStmtNodes = new ArrayList<>(); // to keep track of all bodystatements
		ArrayList<Boolean> isFunctionCall = new ArrayList<>(); // to track which statements are function calls
		while (!tokens.isEmpty()) {
			// lookahead, check if next token is a possible body statement.
			Token token = tokens.get(0);
			if (token.getToken().equals("If") ||
					token.getToken().equals("While") ||
					token.getTokenType() == TokenType.FC_HEADER ||
					(token.getTokenType() == TokenType.ID_KEYWORD &&
							!token.getToken().equals("Return") &&
							!token.getToken().equals("Integer") &&
							!token.getToken().equals("String") &&
							!token.getToken().equals("Double") &&
							!token.getToken().equals("Boolean"))) {
				JottTree bodyStmt = BodyStmtNode.parseBodyStmtNode(tokens);
				bodyStmtNodes.add(bodyStmt);

				// Track if this is a function call
				boolean isFuncCall = (token.getTokenType() == TokenType.FC_HEADER);
				isFunctionCall.add(isFuncCall);

				// If it's a function call, consume the semicolon
				if (isFuncCall) {
					if (tokens.isEmpty()) {
						throw new ParseException("parseBodyNode: Expected ';' after function call", null);
					}
					Token semicolon = tokens.get(0);
					if (semicolon.getTokenType() != TokenType.SEMICOLON) {
						throw new ParseException(
								"parseBodyNode: Expected ';' after function call, got '" + semicolon.getToken() + "'",
								semicolon);
					}
					tokens.remove(0); // consume semicolon
				}
			} else {
				break; // else, it reached return_stmt or end of block
			}
		}

		ReturnStmtNode returnStmt = ReturnStmtNode.parseReturnStmtNode(tokens); // Parse return statement, if tokens is
																				// empty
																				// returnStmtNode will catch error.
		return new BodyNode(bodyStmtNodes, isFunctionCall, returnStmt);
	}

	@Override
	public String convertToJott() {
		// < body > -> < body_stmt >⋆ < return_stmt >
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.bodyStmtNodes.size(); i++) {
			sb.append(this.bodyStmtNodes.get(i).convertToJott());
			// Add semicolon after function calls
			if (this.isFunctionCall.get(i)) {
				sb.append(";");
			}
		}
		sb.append(returnStmt.convertToJott());
		return sb.toString();
	}

	@Override
	public String convertToJava(String indent) {
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
		// default to Void if caller doesn’t pass a type
		return validateTree("Void");
	}

	// used by FBodyNode / FunctionDefNode
	public boolean validateTree(String expectedReturnType) {
		boolean ok = true;

		// validate each statement in the body
		for (JottTree stmt : bodyStmtNodes) {
			ok &= stmt.validateTree();
		}

		// validate the (optional) return statement with knowledge of the expected type
		if (returnStmt != null) {
			ok &= returnStmt.validateTree(expectedReturnType);
		}

		return ok;
	}
}
