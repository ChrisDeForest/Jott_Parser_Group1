package parser;

import provided.JottTree;

public interface ExpressionNode extends JottTree {
	
    public static ExpressionNode parseExpressionNode() {
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
