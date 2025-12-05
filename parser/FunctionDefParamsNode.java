// parser/FunctionDefParamsNode.java
package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;
import java.util.List;

public class FunctionDefParamsNode implements JottTree {

    // make this PUBLIC and add getters
    public static class ParamDecl {
        final Token name;
        final Token type;

        ParamDecl(Token name, Token type) {
            this.name = name;
            this.type = type;
        }

        public Token getNameToken() {
            return name;
        }

        public Token getTypeToken() {
            return type;
        }

        String toJott() {
            return name.getToken() + ":" + type.getToken();
        }
    }

    private final List<ParamDecl> params = new ArrayList<>();

    public FunctionDefParamsNode() {
    }

    public static FunctionDefParamsNode parseFunctionDefParamsNode(ArrayList<Token> tokens) {
        FunctionDefParamsNode node = new FunctionDefParamsNode();

        if (tokens.isEmpty())
            throw new ParseException("parseFunctionDefParamsNode: Unexpected EOF parsing function parameters", null);

        if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) {
            return node; // epsilon
        }

        // <id> : <type>
        Token idTok = tokens.get(0);
        if (idTok.getTokenType() != TokenType.ID_KEYWORD) {
            throw new ParseException("parseFunctionDefParamsNode: Expected parameter name (id) in function definition",
                    idTok);
        }
        tokens.remove(0);

        if (tokens.isEmpty())
            throw new ParseException("parseFunctionDefParamsNode: Unexpected EOF after parameter name", null);
        if (tokens.get(0).getTokenType() != TokenType.COLON) {
            throw new ParseException("parseFunctionDefParamsNode: Expected ':' after parameter name", tokens.get(0));
        }
        tokens.remove(0);

        Token typeTok = expectType(tokens);
        node.params.add(new ParamDecl(idTok, typeTok));

        // (, <id> : <type>)*
        while (!tokens.isEmpty() && tokens.get(0).getTokenType() == TokenType.COMMA) {
            FunctionsDefParamsTNode tail = FunctionsDefParamsTNode.parseFunctionsDefParamsTNode(tokens);
            node.params.add(new ParamDecl(tail.getIdTok(), tail.getTypeTok()));
        }

        return node;
    }

    private static Token expectType(ArrayList<Token> tokens) {
        if (tokens.isEmpty())
            throw new ParseException("parseFunctionDefParamsNode: Unexpected EOF after ':'", null);
        Token t = tokens.get(0);
        String s = t.getToken();
        if (!("Double".equals(s) || "Integer".equals(s) || "String".equals(s) || "Boolean".equals(s))) {
            throw new ParseException(
                    "parseFunctionDefParamsNode: Expected a type (Double | Integer | String | Boolean)", t);
        }
        tokens.remove(0);
        return t;
    }

    // NEW: what FunctionDefNode.validateTree() iterates
    public List<ParamDecl> getParamEntries() {
        return params;
    }

    // keep your existing helper if other code uses it
    public List<Token> getParamTypeTokens() {
        List<Token> out = new ArrayList<>();
        for (var p : params)
            out.add(p.type);
        return out;
    }

    @Override
    public String convertToJott() { /* unchanged */
        /* ... */
        if (params.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(params.get(i).toJott());
        }
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
        // basic duplicate-name check (types already validated in parse)
        boolean ok = true;
        java.util.HashSet<String> seen = new java.util.HashSet<>();
        for (ParamDecl p : params) {
            if (!seen.add(p.name.getToken())) {
                System.err.println("Semantic Error\nDuplicate parameter " + p.name.getToken()
                        + "\n" + p.name.getFilename() + ":" + p.name.getLineNum());
                ok = false;
            }
        }
        return ok;
    }

    public Object evaluate(){
        return null;
    } 
}
