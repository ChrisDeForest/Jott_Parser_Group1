package parser;

import provided.JottTree;
import java.util.ArrayList;
import provided.Token;

public class ProgramNode implements JottTree {

    public ProgramNode() {
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens) {
        return new ProgramNode();
    }


    @Override
    public String convertToJott() {
        return null;
    }

    @Override
    public String convertToJava(String classname) {
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


