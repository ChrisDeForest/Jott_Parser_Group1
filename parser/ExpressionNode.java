package parser;

import java.util.ArrayList;

import provided.Token;
import provided.JottTree;
import provided.TokenType;

public interface ExpressionNode extends JottTree {
		// < expr > -> < operand > | < operand > < relop > < operand > |
		//				< operand > < mathop > < operand > | < string_literal > |
		//				< bool >
	    public static ExpressionNode parseExpressionNode(ArrayList<Token> tokens) {
		// initial check for empty list
		if (tokens.isEmpty()) throw new ParseException("parseExpressionNode: Unexpected EOF", null);
		
		Token t = tokens.get(0);
		// checking string literal
		if (t.getTokenType().equals(TokenType.STRING)){	
			return StringLiteralNode.parseStringLiteralNode(tokens);
		}
		// checking bool
		String tToken = t.getToken().toLowerCase();		
		if (tToken.equals("true") || tToken.equals("false")){
			return BoolNode.parseBoolNode(tokens);
		}

		// parsing left operand
		if (tokens.isEmpty()) throw new ParseException("parseExpressionNode: Unexpected EOF", null);
		OperandNode left = OperandNode.parseOperand(tokens);
		if (left == null) throw new ParseException("parseExpressionNode: Error parsing left operand", null);
		if (tokens.isEmpty()) return left;	// if only one operand left, return <operand>
		
		Token operand = tokens.get(0);
		if (operand.getTokenType().equals(TokenType.MATH_OP) || operand.getTokenType().equals(TokenType.REL_OP)) {
			// Consume the operator token
			tokens.remove(0);
			String opToken = operand.getToken();
			
			// parsing right operand
			if (tokens.isEmpty()) throw new ParseException("parseExpressionNode: Unexpected EOF", null);
			OperandNode right = OperandNode.parseOperand(tokens);
			if (right == null) throw new ParseException("parseExpressionNode: Error parsing right operand", null);

			return new ExpressionNode() {
				@Override public String convertToJott(){
					StringBuilder sb = new StringBuilder();
					sb.append(left.convertToJott());
					sb.append(" " + opToken + " ");
					sb.append(right.convertToJott());
					return sb.toString();
				}
				@Override public boolean validateTree(){ return false;}
				@Override public String convertToJava(String indentLevel){ return null;}
				@Override public String convertToC(){ return null; }
				@Override public String convertToPython(){ return null;}
			};
		} else {
			// No operator found, return single operand
			return left;
		}
    }

	@Override
	public String convertToJott();

	@Override
	public String convertToJava(String indentLevel);

	@Override
	public boolean validateTree();

	@Override
	public String convertToC();

	@Override
	public String convertToPython();
}
