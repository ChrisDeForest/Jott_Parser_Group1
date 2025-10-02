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
         
        if (tokens == null || tokens.size() == 0) {
            System.err.println("parseFunctionCallNode: expected function call but no tokens available");
            return null;
        }
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.FC_HEADER) {
            System.err.println("parseFunctionCallNode: expected function name token, got '" + t.getToken() + "' at " + t.getFilename() + ":" + t.getLineNum());
            return null;
        }

        // consume the token and return a new FunctionCallNode
        tokens.remove(0);
        IDNode functionName = IDNode.parseIDNode(tokens);
        tokens.remove(0);
        ParamNode params = ParamNode.parseParamNode(tokens);
        tokens.remove(0);
        
        return new FunctionCallNode(t, functionName, params);
    }

    public String convertToJott() {
        return this.functionHeaderToken.getToken() + " " + 
        this.functionName.convertToJott() + " " + 
        this.params.convertToJott();
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
