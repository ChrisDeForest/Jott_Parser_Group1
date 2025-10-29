package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ParamNode implements JottTree {

    private final List<JottTree> args; // each is an ExpressionNode

    public ParamNode(List<JottTree> args) {
        this.args = args;
    }

    // <params> -> <expr> <params_t>* | ε
    public static ParamNode parseParamNode(ArrayList<Token> tokens) {
        List<JottTree> list = new ArrayList<>();

        if (tokens.isEmpty())
            throw new ParseException("parseParamNode: Unexpected EOF parsing function call params", null);

        // epsilon if next is R_BRACKET
        if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) {
            return new ParamNode(list);
        }

        // First <expr>
        JottTree first = ExpressionNode.parseExpressionNode(tokens);
        list.add(first);

        // Zero or more tails
        while (!tokens.isEmpty() && tokens.get(0).getTokenType() == TokenType.COMMA) {
            ParamTNode tail = ParamTNode.parseParamTNode(tokens);
            list.add(tail.expr);
        }

        return new ParamNode(list);
    }

    @Override
    public String convertToJott() {
        if (args.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(args.get(i).convertToJott());
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

    public List<JottTree> getArgs() {
        return args;
    }
}
