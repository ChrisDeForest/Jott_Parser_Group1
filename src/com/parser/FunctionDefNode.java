package parser;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;

public class FunctionDefNode implements JottTree {
    public FunctionDefNode() {
    }

    public static FunctionDefNode parseFunctionDefNode(ArrayList<Token> tokens) {
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
