package parser;
import java.util.ArrayList;

import provided.JottTree;
import provided.Token;

public class ElseIfNode implements JottTree {
    public ElseIfNode() {
    }

    public static ElseIfNode parseElseIfNode(ArrayList<Token> tokens) {
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
