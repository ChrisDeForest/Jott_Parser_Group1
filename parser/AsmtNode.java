package parser;

import java.util.ArrayList;

import execution.RuntimeEnv;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import semantics.*;

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
		expr.validateTree();
		id.validateTree();

		// check if variable is declared
		String varName = id.getName();
		if (!SymbolTable.variableExists(varName)) {
			throw new SemanticException("AsmtNode: Variable '" + varName + "' is not declared.", null);
		}
		
		String varType = SymbolTable.getVariableType(varName);
		String exprType = expr.getType(SymbolTable.globalSymbolTable);


		// check if types match
		if (!varType.equals(exprType)) {
			throw new SemanticException("AsmtNode: Type mismatch in assignment to variable '" + varName + "'. Expected '" + varType + "', but got '" + exprType + "'.", null);
		}

		// mark variable as initialized
		SymbolTable.initializeVariable(varName);
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
	public Object evaluate(){
		Object value = expr.evaluate();
		String name = id.getName();

		RuntimeEnv.setVariable(name, value); // update runtime env, initalize variable

		return null;
	}
}
