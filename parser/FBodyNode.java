package parser;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class FBodyNode implements JottTree {
    private final ArrayList<VarDecNode> varDecs;
    private final BodyNode body;
    
    public FBodyNode(ArrayList<VarDecNode> varDecs, BodyNode body) {
        this.varDecs = varDecs;
        this.body = body;
    }

    public static FBodyNode parseFBodyNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) {
			throw new ParseException("parseFBodyNody: Unexpected EOF while parsing <f_body>", null);
		}
        ArrayList<VarDecNode> varDecs = new ArrayList<>();

        // Parse zero or more variable declarations (Kleene star)
        // A variable declaration starts with a type keyword (Double, Integer, String, or Boolean)
        while (!tokens.isEmpty()) {
            Token t = tokens.get(0);
            
            // Check if this is a type keyword (start of var_dec)
            if (t.getTokenType() == TokenType.ID_KEYWORD && 
                (t.getToken().equals("Double") || t.getToken().equals("Integer") || 
                 t.getToken().equals("String") || t.getToken().equals("Boolean"))) {
                // Will throw ParseException if it fails
                VarDecNode varDec = VarDecNode.parseVarDecNode(tokens);
                varDecs.add(varDec);
            } else {
                // Not a variable declaration, break to parse body
                break;
            }
        }

        // Parse body (will throw ParseException if it fails)
        BodyNode body = BodyNode.parseBodyNode(tokens);

        return new FBodyNode(varDecs, body);
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        for (VarDecNode varDec : varDecs) {
            sb.append(varDec.convertToJott());
        }
        sb.append(body.convertToJott());
        return sb.toString();
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
