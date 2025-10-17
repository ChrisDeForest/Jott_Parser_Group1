package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;
import java.util.List;

public class FunctionDefParamsNode implements JottTree {

    // simple holder for name:type
    private static class ParamDecl {
        final Token name;
        final Token type;

        ParamDecl(Token name, Token type) {
            this.name = name;
            this.type = type;
        }

        String toJott() {
            return name.getToken() + ":" + type.getToken();
        }
    }

    private final List<ParamDecl> params = new ArrayList<>();

    public FunctionDefParamsNode() {
    }

    // NEW: accept tokens; parse <id> : <type> ( , <id> : <type> )* | ε
    public static FunctionDefParamsNode parseFunctionDefParamsNode(ArrayList<Token> tokens) {
        FunctionDefParamsNode node = new FunctionDefParamsNode();

        if (tokens.isEmpty())
            throw new ParseException("Unexpected EOF parsing function parameters", null);

        // epsilon: if next is ']' we have no params
        if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) {
            return node;
        }

        // first param: <id> : <type>
        Token idTok = tokens.get(0);
        if (idTok.getTokenType() != TokenType.ID_KEYWORD) {
            throw new ParseException("Expected parameter name (id) in function definition", idTok);
        }
        tokens.remove(0); // consume id

        if (tokens.isEmpty())
            throw new ParseException("Unexpected EOF after parameter name", null);
        if (tokens.get(0).getTokenType() != TokenType.COLON) {
            throw new ParseException("Expected ':' after parameter name", tokens.get(0));
        }
        tokens.remove(0); // consume ':'

        Token typeTok = expectType(tokens);
        node.params.add(new ParamDecl(idTok, typeTok));

        // Zero or more tails: , <id> : <type>
        while (!tokens.isEmpty() && tokens.get(0).getTokenType() == TokenType.COMMA) {
            FunctionsDefParamsTNode tail = FunctionsDefParamsTNode.parseFunctionsDefParamsTNode(tokens);
            node.params.add(new ParamDecl(tail.getIdTok(), tail.getTypeTok()));
        }

        return node;
    }

    private static Token expectType(ArrayList<Token> tokens) {
        if (tokens.isEmpty())
            throw new ParseException("Unexpected EOF after ':'", null);
        Token t = tokens.get(0);
        String s = t.getToken();
        if (!("Double".equals(s) || "Integer".equals(s) || "String".equals(s) || "Boolean".equals(s))) {
            throw new ParseException("Expected a type (Double | Integer | String | Boolean)", t);
        }
        tokens.remove(0);
        return t;
    }

    @Override
    public String convertToJott() {
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
        return true;
    }
}
