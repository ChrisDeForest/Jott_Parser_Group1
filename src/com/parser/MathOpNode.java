package parser;

import provided.Token;
import provided.TokenType;
import java.util.ArrayList;

public class MathOpNode implements ExpressionNode{

    private final Token mathOpToken;

    public MathOpNode(Token mathOpToken) {
        this.mathOpToken = mathOpToken;
    }

    public static MathOpNode parseMathOpNode(ArrayList<Token> tokens) {
        Token t = tokens.remove(0);
        if (t.getTokenType() == TokenType.MATH_OP) {
            return new MathOpNode(t);
        }
        return null;
    }

    @Override
	public String convertToJott() {
		return this.mathOpToken.getToken();
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
