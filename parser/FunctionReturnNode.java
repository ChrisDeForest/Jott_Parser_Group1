package parser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

public class FunctionReturnNode implements JottTree {
    private final Token returnTypeToken;
    private final boolean isVoid;

    public FunctionReturnNode(Token returnTypeToken, boolean isVoid) {
        this.returnTypeToken = returnTypeToken;
        this.isVoid = isVoid;
    }

    public static FunctionReturnNode parseFunctionReturnNode(ArrayList<Token> tokens) {
		// <function_return> -> <type> | Void
        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionReturnNode: Unexpected EOF while parsing function return type", null);
        }
        
        Token token = tokens.get(0);
        
        // Check if it's "Void"
        if (token.getTokenType() == TokenType.ID_KEYWORD && token.getToken().equals("Void")) {
            tokens.remove(0); // consume "Void"
            return new FunctionReturnNode(token, true);
        }
        
        // Check if it's a valid type (Double, Integer, String, Boolean)
        if (token.getTokenType() == TokenType.ID_KEYWORD) {
            String tokenValue = token.getToken();
            if (tokenValue.equals("Double") || tokenValue.equals("Integer") || 
                tokenValue.equals("String") || tokenValue.equals("Boolean")) {
                tokens.remove(0); // consume the type token
                return new FunctionReturnNode(token, false);
            }
        }
        
        // If we get here, it's neither Void nor a valid type
        throw new ParseException("parseFunctionReturnNode: Expected return type (Double, Integer, String, Boolean, or Void), got '" + token.getToken() + "'", token);
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
		return returnTypeToken.getToken();
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
