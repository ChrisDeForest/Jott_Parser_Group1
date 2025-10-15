package parser;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;
public class ParamNode implements JottTree{
   
    public ParamNode() {
    }

    public static ParamNode parseParamNode(ArrayList<Token> tokens) {
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
