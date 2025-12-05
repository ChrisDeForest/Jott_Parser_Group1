package parser;

import java.util.ArrayList;

import execution.RuntimeEnv;
import provided.Token;
import provided.JottTree;
import provided.TokenType;
import semantics.SemanticException;
import semantics.SymbolTable;

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
		String tToken = t.getToken();
		if (tToken.equals("True") || tToken.equals("False")){
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
			@Override public String getType(SymbolTable symbolTable) {
				String leftType = left.getType(symbolTable);
				String rightType = right.getType(symbolTable);

				if (leftType == null || rightType == null) {
					return null;
				}

				if (!leftType.equals(rightType)) {
					return null;
				}

				if (operand.getTokenType().equals(TokenType.REL_OP)) {
					// Inequality comparisons require numeric operands
					if (opToken.equals("<") || opToken.equals(">") ||
						opToken.equals("<=") || opToken.equals(">=")) {
						if (!"Integer".equals(leftType) && !"Double".equals(leftType)) {
							return null;
						}
					}
					return "Boolean";
				}
				// For math operators, determine result type from operands
				if (operand.getTokenType().equals(TokenType.MATH_OP)) {
					if (!"Integer".equals(leftType) && !"Double".equals(leftType)) {
						return null;
					}
					return leftType;
				}
				return null; // Type error
			}
			@Override public boolean validateTree(){ 
				// Validate both operands
				boolean ok = left.validateTree();
				ok &= right.validateTree();

				String leftType = left.getType(SymbolTable.globalSymbolTable);
				String rightType = right.getType(SymbolTable.globalSymbolTable);

				if (leftType == null || rightType == null) {
					throw new SemanticException("ExpressionNode: Unable to determine operand types in expression.", null);
				}

				if (!leftType.equals(rightType)) {
					throw new SemanticException("ExpressionNode: Type mismatch in expression. Left type '" + leftType
							+ "', right type '" + rightType + "'.", null);
				}

				if (operand.getTokenType().equals(TokenType.MATH_OP)) {
					if (!"Integer".equals(leftType) && !"Double".equals(leftType)) {
						throw new SemanticException("ExpressionNode: Math operations require numeric operands.", null);
					}
				}

				if (operand.getTokenType().equals(TokenType.REL_OP)) {
					if ((opToken.equals("<") || opToken.equals(">") ||
							opToken.equals("<=") || opToken.equals(">=")) &&
							(!"Integer".equals(leftType) && !"Double".equals(leftType))) {
						throw new SemanticException("ExpressionNode: Relational operator '" + opToken
								+ "' requires numeric operands.", null);
					}
				}

				if (getType(SymbolTable.globalSymbolTable) == null) {
					throw new SemanticException("ExpressionNode: Invalid operand types for operator '" + opToken + "'.", null);
				}

				return ok;
			}
			@Override public String convertToJava(String indentLevel){ return null;}
			@Override public String convertToC(){ return null; }
			@Override public String convertToPython(){ return null;}

			@Override
			public Object evaluate(){
				Object leftVal = left.evaluate();
				Object rightVal = right.evaluate();

				switch (opToken) {
					case "+":
						if (leftVal instanceof Integer) {
							return (Integer) leftVal + (Integer) rightVal;
						}
						if (leftVal instanceof Double) {
							return (Double) leftVal + (Double) rightVal;
						}
						break;
					case "-":
						if (leftVal instanceof Integer) {
							return (Integer) leftVal - (Integer) rightVal;
						}
						if (leftVal instanceof Double) {
							return (Double) leftVal - (Double) rightVal;
						}
						break;
					case "*":
						if (leftVal instanceof Integer) {
							return (Integer) leftVal * (Integer) rightVal;
						}
						if (leftVal instanceof Double) {
							return (Double) leftVal * (Double) rightVal;
						}
						break;
					case "/":
						if (leftVal instanceof Integer) {
							if ((Integer) rightVal == 0) {
								throw new RuntimeException("ExpressionNode: Division by Zero");
							}
							return (Integer) leftVal / (Integer) rightVal;
						}
						if (leftVal instanceof Double) {
							if ((Double) rightVal == 0.0) {
								throw new RuntimeException("ExpressionNode: Division by Zero");
							}
							return (Double) leftVal / (Double) rightVal;
						}
						break;
					case "==":
						return leftVal.equals(rightVal);
					case "!=":
						return !leftVal.equals(rightVal);
					case "<":
						if (leftVal instanceof Integer) {
							return (Integer) leftVal < (Integer) rightVal;
						}
						if (leftVal instanceof Double) {
							return (Double) leftVal < (Double) rightVal;
						}
						break;
					case ">":
						if (leftVal instanceof Integer) {
							return (Integer) leftVal > (Integer) rightVal;
						}
						if (leftVal instanceof Double) {
							return (Double) leftVal > (Double) rightVal;
						}
						break;
				}

				return null;
			}
		};
		} else {
			// No operator found, return single operand
			return left;
		}
    }
	/**
	 * Get the type of this expression
	 * @param symbolTable The symbol table for looking up variable and function types
	 * @return The type string ("Integer", "Double", "String", "Boolean"), or null if type cannot be determined
	 */
	public String getType(SymbolTable symbolTable);

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

	@Override
	public Object evaluate();
}
