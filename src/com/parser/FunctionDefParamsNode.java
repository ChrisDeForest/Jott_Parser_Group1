package parser;
import provided.JottTree;

public class FunctionDefParamsNode implements JottTree {
    public FunctionDefParamsNode() {
    }

    public static FunctionDefParamsNode parseFunctionDefParamsNode() {
        return new FunctionDefParamsNode();
    }

    @Override
    public String convertToJott() {
        return null;
    }

    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return false;
    }
    
}
