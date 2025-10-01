package parser;

import provided.JottTree;

public interface BodyStmtNode extends JottTree { 

    public static BodyStmtNode parseBodyStmtNode() {
        return null;
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
