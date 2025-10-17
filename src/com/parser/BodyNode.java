package parser;
import java.util.ArrayList;

import provided.*;

public class BodyNode implements JottTree{
	private final ArrayList<JottTree> bodyStmtNodes;
	private final ReturnStmtNode returnStmt;

    public BodyNode(ArrayList<JottTree> bodyStmtNodes, ReturnStmtNode returnStmt) {
		this.bodyStmtNodes = bodyStmtNodes;
		this.returnStmt = returnStmt;
	}

	public static BodyNode parseBodyNode(ArrayList<Token> tokens) {
		// < body > -> < body_stmt >⋆ < return_stmt >
		if (tokens.isEmpty()) {
			throw new ParseException("Unexpected EOF while parsing <body>", null);
		}

		ArrayList<JottTree> bodyStmtNodes = new ArrayList<>(); // to keep track of all bodystatements
		while (!tokens.isEmpty()) {
			// lookahead, check if next token is a possible body statement.
			Token token = tokens.get(0);
			if (token.getToken().equals("If") ||
				token.getToken().equals("While") ||
				token.getTokenType() == TokenType.FC_HEADER ||
				token.getTokenType() == TokenType.ID_KEYWORD) {
				bodyStmtNodes.add(BodyStmtNode.parseBodyStmtNode(tokens)); // if it is, parse
			} else {
				break; // else, it reached return_stmt or end of block
			}
		}

		ReturnStmtNode returnStmt = ReturnStmtNode.parseReturnStmtNode(tokens); // Parse return statement, if tokens is empty 
																				// returnStmtNode will catch error.
		return new BodyNode(bodyStmtNodes, returnStmt);
	}

	@Override
	public String convertToJott() {
		// < body > -> < body_stmt >⋆ < return_stmt >
		StringBuilder sb = new StringBuilder();
        for (JottTree bodyStmt : this.bodyStmtNodes) {
            sb.append(bodyStmt.convertToJott());
        }
        sb.append(returnStmt.convertToJott());
        return sb.toString();
	}

	@Override
	public String convertToJava(String indent) {
		// TODO: Implement method logic
		return "";
	}

	@Override
	public boolean validateTree() {
		// TODO: Implement method logic
		return true;
	}

	@Override
	public String convertToC() {
		// TODO: Implement method logic
		return "";
	}

	@Override
	public String convertToPython() {
		// TODO: Implement method logic
		return "";
	}
}
