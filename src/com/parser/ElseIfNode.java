package parser;
import provided.JottTree;

public class ElseIfNode implements JottTree {
    public ElseIfNode() {
    }

    public static ElseIfNode parseElseIfNode() {
        return new ElseIfNode();
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
