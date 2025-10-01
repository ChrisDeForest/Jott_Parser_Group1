package parser;
import provided.JottTree;


public class TypeNode implements JottTree{
    
    public TypeNode() {
    }

    public static TypeNode parseTypeNode() {
        return new TypeNode();
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
