package parser;
import provided.JottTree;

public class ParamTNode implements JottTree{
   
    public ParamTNode() {
    }

    public static ParamTNode parseParamTNode() {
        return new ParamTNode();
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
