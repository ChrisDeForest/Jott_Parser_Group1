package parser;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;

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
        // A variable declaration starts with a type keyword (Double, Integer, String,
        // or Boolean)
        while (!tokens.isEmpty()) {
            Token t = tokens.get(0);
            if (t.getTokenType() == provided.TokenType.ID_KEYWORD &&
                    ("Double".equals(t.getToken()) || "Integer".equals(t.getToken()) ||
                            "String".equals(t.getToken()) || "Boolean".equals(t.getToken()))) {
                VarDecNode varDec = VarDecNode.parseVarDecNode(tokens);
                varDecs.add(varDec);
            } else {
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
        return validateTree("Void");
    }

    // NEW: used by FunctionDefNode
    public boolean validateTree(String expectedReturnType) {
        boolean ok = true;
        for (VarDecNode v : varDecs) {
            ok &= v.validateTree(); // typical place where vars hit the symbol table
        }
        // Have BodyNode enforce statements & return rules knowing the expected type
        ok &= body.validateTree(expectedReturnType);
        return ok;
    }

    

    @Override
    public Object evaluate(){

        for (VarDecNode v: varDecs) {
            v.evaluate(); // declaration happens in varDecNode
        }

        Object result = body.evaluate(); // actually evaluate body

        return result;

    }
}
