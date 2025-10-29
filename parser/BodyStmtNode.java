package parser;
import provided.*;
import java.util.ArrayList;

public interface BodyStmtNode extends JottTree { 

	public static JottTree parseBodyStmtNode(ArrayList<Token> tokens) {
		// <body_stmt > -> <if_stmt > | <while_loop > | <asmt > | <func_call>;
		  
        if (tokens.isEmpty()){
			throw new ParseException("parseBodyStmtNode: Unexpected EOF", null);
		}
		Token token = tokens.get(0);

		if (token.getToken().equals("If")) {
			return IfStmtNode.parseIfStmtNode(tokens);
		} 
		else if (token.getToken().equals("While")) {
			return WhileLoopNode.parseWhileLoopNode(tokens);
		}
		else if (token.getToken().equals("Else")) {
        	throw new ParseException("parseBodyStmtNode: Unexpected 'Else' without preceding 'If'", token);
		}
		else if (token.getToken().equals("Elseif")) {
			throw new ParseException("parseBodyStmtNode: Unexpected 'Elseif' without preceding 'If'", token);
		}
		else if (token.getTokenType() == TokenType.FC_HEADER){
			return FunctionCallNode.parseFunctionCallNode(tokens);
		}
		else if(token.getTokenType() == TokenType.ID_KEYWORD) { // last option would be Asmt node
			return AsmtNode.parseAsmtNode(tokens);
		}
		else {
			throw new ParseException("parseBodyStmtNode: Invalid body statement", token);
		}
	}

	@Override
	public String convertToJava(String indentLevel);

	@Override
	public boolean validateTree();

	@Override
	public String convertToJott();

	@Override
	public String convertToC();

	@Override
	public String convertToPython();
}
