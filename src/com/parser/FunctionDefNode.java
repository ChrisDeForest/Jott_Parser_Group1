package parser;

import provided.JottTree;

public class FunctionDefNode implements JottTree {
    public FunctionDefNode() {
    }

    public static FunctionDefNode parseFunctionDefNode() {
        return new FunctionDefNode();
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
