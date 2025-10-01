package parser;

import provided.JottTree;

public class IfStmtNode implements JottTree{

    public IfStmtNode() {
    }

    public static IfStmtNode parseIfStmtNode() {
        return new IfStmtNode();
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
