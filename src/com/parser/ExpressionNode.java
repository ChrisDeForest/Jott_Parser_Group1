package parser;

import java.util.ArrayList;

import provided.Token;
import provided.JottTree;

public interface ExpressionNode extends JottTree {
	
    public static ExpressionNode parseExpressionNode(ArrayList<Token> tokens) {
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
