package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import java.util.ArrayList;

public class VarDecNode implements JottTree {

    private final Token varDecToken;
    private final TypeNode typeNode;
    private final IDNode idNode;
    
    public VarDecNode(Token varDecToken) {
        this.varDecToken = varDecToken;
    }

    public static VarDecNode parseVarDecNode(ArrayList<Token> tokens) {
        parseTypeNode(tokens);
        parseIdNode(tokens);
        return new VarDecNode();
    }

    @Override
    public String convertToJava(String indent) {
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
