package parser;

import java.util.ArrayList;

import provided.Token;
import provided.TokenType;

public interface OperandNode extends ExpressionNode {
    public static OperandNode parseOperand(ArrayList<Token> tokens){
        Token t = tokens.get(0);

        if(t.getTokenType().equals(TokenType.ID_KEYWORD)){
            return IDNode.parseIDNode(tokens);
        }
        else if(t.getTokenType().equals(TokenType.NUMBER)){
            return NumberNode.parseNumberNode(tokens, false);
        }
        else if(t.getTokenType().equals(TokenType.FC_HEADER)){
            return FunctionCallNode.parseFunctionCallNode(tokens);
        }
        else if(t.getTokenType().equals(TokenType.MATH_OP) && t.getToken().equals("-")){
            tokens.remove(0);
            t = tokens.get(0);
            if(t.getTokenType().equals(TokenType.NUMBER)){
                return NumberNode.parseNumberNode(tokens, true);
            }
        }
        throw new ParseException("parseOperand: Expected <exp>, got <end_stmt>", t);
    }

    public String convertToJott();

    public String convertToJava(String classname);

    public String convertToC();

    public String convertToPython();

    public boolean validateTree();
}
