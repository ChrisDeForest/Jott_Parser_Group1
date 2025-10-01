package parser;

import provided.JottTree;

public class ExpressionNode implements JottTree {

    public ExpressionNode() { 
    }

    public static ExpressionNode parseExpressionNode() {
        return new ExpressionNode();
    }

	@Override
	public String convertToJava(String indentLevel) {
		// TODO: Implement conversion logic
		return "";
	}

	@Override
	public boolean validateTree() {
		// TODO: Implement validation logic
		return true;
	}

	@Override
	public String convertToJott() {
		// TODO: Implement conversion logic
		return "";
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
