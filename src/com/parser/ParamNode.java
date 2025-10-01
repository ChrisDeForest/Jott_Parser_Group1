package parser;

import provided.JottTree;

public class ParamNode implements JottTree{
   
    public ParamNode() {
    }

    public static ParamNode parseParamNode() {
        return new ParamNode();
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
