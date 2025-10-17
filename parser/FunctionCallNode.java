package parser;

import provided.TokenType;
import provided.Token;
import java.util.ArrayList;

public class FunctionCallNode implements OperandNode {
    private final Token functionHeaderToken;
    private final IDNode functionName;
    private final ParamNode params;

    public FunctionCallNode(Token functionHeaderToken, IDNode functionName, ParamNode params) {
        this.functionHeaderToken = functionHeaderToken;
        this.functionName = functionName;
        this.params = params;
    }

    public static FunctionCallNode parseFunctionCallNode(ArrayList<Token> tokens) {
         // < func_call > -> :: < id >[ < params >]
         
        if (tokens == null || tokens.isEmpty()) throw new ParseException("Unexpected EOF", null);
        
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.FC_HEADER) throw new ParseException("Missing" + TokenType.FC_HEADER + " to begin a function call", t);
        
        // consume the token and return a new FunctionCallNode
        tokens.remove(0);
        IDNode functionName = IDNode.parseIDNode(tokens);

        // Check for opening bracket ([)
		if (tokens.isEmpty()) throw new ParseException("Unexpected EOF", null);
		if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) throw new ParseException("Missing '[' after function name", tokens.get(0));
	    
        // consume ([)
		tokens.remove(0); 
		ParamNode params = ParamNode.parseParamNode(tokens);

		// Check for closing bracket (])
		if (tokens.isEmpty()) throw new ParseException("Unexpected EOF", null);
		if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) throw new ParseException("Missing ']' after function call parameters", tokens.get(0));
		tokens.remove(0); // consume (])

        return new FunctionCallNode(t, functionName, params);
    }

    public String convertToJott() {
         // < func_call > -> :: < id >[ < params >]
        return this.functionHeaderToken.getToken() + " " + 
        this.functionName.convertToJott() + "[" +
        this.params.convertToJott() + "]"; 
    }

    public String convertToJava(String classname) {
        return null;
    }
    public String convertToC() {
        return null;
    }
    public String convertToPython() {
        return null;
    }
    public boolean validateTree() {
        return false;
    }
}
