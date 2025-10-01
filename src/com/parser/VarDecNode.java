package parser;
import provided.JottTree;

public class VarDecNode implements JottTree {
    
    public VarDecNode() {
    }

    public static VarDecNode parseVarDecNode() {
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
