package parser;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class RelOpNode implements ExpressionNode {

    private final Token relOp;

    public RelOpNode(Token relOp) {
        this.relOp = relOp;
    }

    public static RelOpNode parseRelOpNode(ArrayList<Token> tokens) {
        Token t = tokens.remove(0);
        if (t.getTokenType() == TokenType.REL_OP) {
            return new RelOpNode(t);
        }
        return null;
    }

    @Override
	public String convertToJott() {
		return this.relOp.getToken();
	}

    @Override
	public String convertToJava(String indentLevel) {
		// TODO: Implement this method
		return "";
	}

	@Override
	public boolean validateTree() {
		// TODO: Implement this method
		return true;
	}

	

	@Override
	public String convertToC() {
		// TODO: Implement this method
		return "";
	}

	@Override
	public String convertToPython() {
		// TODO: Implement this method
		return "";
	}
}
