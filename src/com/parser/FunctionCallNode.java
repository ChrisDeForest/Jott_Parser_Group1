package parser;

import provided.JottTree;
import provided.TokenType;
import provided.Token;
import java.util.ArrayList;

public class FunctionCallNode implements JottTree {
    private final String functionName;

    public FunctionCallNode(String functionName) {
        this.functionName = functionName;
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
        return new FunctionCallNode(t.getToken());
    }

    public String convertToJott() {
        return this.functionName;
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
