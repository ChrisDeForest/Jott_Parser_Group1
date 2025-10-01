package parser;

import provided.JottTree;

public class WhileLoopNode implements JottTree{

    public WhileLoopNode() {
    }

    public static WhileLoopNode parseWhileLoopNode() {
        return new WhileLoopNode();
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
