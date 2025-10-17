package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;
import provided.ParseException;
import java.util.ArrayList;

public class IfStmtNode implements JottTree {
    private final ExpressionNode condition;
    private final BodyNode body;
    private final ArrayList<ElseIfNode> elseIfList;
    private final ElseNode elseNode;

    public IfStmtNode(ExpressionNode condition, BodyNode body, ArrayList<ElseIfNode> elseIfList, ElseNode elseNode) {
        this.condition = condition;
        this.body = body;
        this.elseIfList = elseIfList;
        this.elseNode = elseNode;
    }

    public static IfStmtNode parseIfStmtNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF while parsing <if_stmt>", null);
        }

        // Parse "If" keyword
        Token ifToken = tokens.get(0);
        if (ifToken.getTokenType() != TokenType.ID_KEYWORD || !ifToken.getToken().equals("If")) {
            throw new ParseException("Expected 'If', got '" + ifToken.getToken() + "'", ifToken);
        }
        tokens.remove(0);

        // Parse left bracket [
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF", null);
        }
        Token leftBracket = tokens.get(0);
        if (leftBracket.getTokenType() != TokenType.L_BRACKET) {
            throw new ParseException("Expected '[' after 'If', got '" + leftBracket.getToken() + "'", leftBracket);
        }
        tokens.remove(0);

        // Parse expression (will throw ParseException if it fails)
        ExpressionNode condition = ExpressionNode.parseExpressionNode(tokens);

        // Parse right bracket ]
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF", null);
        }
        Token rightBracket = tokens.get(0);
        if (rightBracket.getTokenType() != TokenType.R_BRACKET) {
            throw new ParseException("Expected ']' after condition, got '" + rightBracket.getToken() + "'", rightBracket);
        }
        tokens.remove(0);

        // Parse left brace {
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF", null);
        }
        Token leftBrace = tokens.get(0);
        if (leftBrace.getTokenType() != TokenType.L_BRACE) {
            throw new ParseException("Expected '{' after condition, got '" + leftBrace.getToken() + "'", leftBrace);
        }
        tokens.remove(0);

        // Parse body (will throw ParseException if it fails)
        BodyNode body = BodyNode.parseBodyNode(tokens);

        // Parse right brace }
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF", null);
        }
        Token rightBrace = tokens.get(0);
        if (rightBrace.getTokenType() != TokenType.R_BRACE) {
            throw new ParseException("Expected '}' after body, got '" + rightBrace.getToken() + "'", rightBrace);
        }
        tokens.remove(0);

        // Parse zero or more ElseIf nodes (Kleene star)
        ArrayList<ElseIfNode> elseIfList = new ArrayList<>();
        while (!tokens.isEmpty()) {
            Token t = tokens.get(0);
            if (t.getTokenType() == TokenType.ID_KEYWORD && t.getToken().equals("Elseif")) {
                // Will throw ParseException if it fails
                ElseIfNode elseIfNode = ElseIfNode.parseElseIfNode(tokens);
                elseIfList.add(elseIfNode);
            } else {
                break;
            }
        }

        // Parse Else node (which can be epsilon, will throw ParseException if it fails)
        ElseNode elseNode = ElseNode.parseElseNode(tokens);

        return new IfStmtNode(condition, body, elseIfList, elseNode);
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        sb.append("If[");
        sb.append(condition.convertToJott());
        sb.append("]{");
        sb.append(body.convertToJott());
        sb.append("}");
        
        // Add all elseif nodes
        for (ElseIfNode elseIf : elseIfList) {
            sb.append(elseIf.convertToJott());
        }
        
        // Add else node
        sb.append(elseNode.convertToJott());
        
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
