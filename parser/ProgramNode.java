package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;
import java.util.ArrayList;
public class ProgramNode implements JottTree {
    private final ArrayList<FunctionDefNode> functionDefs;

    public ProgramNode(ArrayList<FunctionDefNode> functionDefs) {
        this.functionDefs = functionDefs;
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens) {
        ArrayList<FunctionDefNode> functionDefs = new ArrayList<>();

        // Parse zero or more function definitions (Kleene star)
        while (!tokens.isEmpty()) {
            Token t = tokens.get(0);
            
            if (t.getTokenType() == TokenType.ID_KEYWORD && t.getToken().equals("Def")) {
                // Will throw ParseException if it fails
                FunctionDefNode funcDef = FunctionDefNode.parseFunctionDefNode(tokens);
                functionDefs.add(funcDef);
            } else {
                break;
            }
        }

        // At this point, we should have consumed all tokens (EOF)
        if (!tokens.isEmpty()) {
            throw new ParseException("parseProgramNode: Unexpected tokens after function definitions: '" + tokens.get(0).getToken() + "'", tokens.get(0));
        }

        return new ProgramNode(functionDefs);
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        for (FunctionDefNode funcDef : functionDefs) {
            sb.append(funcDef.convertToJott());
        }
        return sb.toString();
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
