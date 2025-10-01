package parser;
import provided.JottTree;

public class ElseNode implements JottTree{

    public ElseNode() {
    }

    public static ElseNode parseElseNode() {
        return new ElseNode();
    }

    @Override
    public String convertToJava(String indent) {
        // TODO: Implement conversion logic
        return "";
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

    @Override
    public boolean validateTree() {
        // TODO: Implement validation logic
        return true;
    }
}
