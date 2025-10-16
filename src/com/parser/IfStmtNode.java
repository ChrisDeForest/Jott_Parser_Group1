package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;
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
        if (tokens == null || tokens.size() == 0) {
            System.err.println("parseIfStmtNode: expected 'If' but no tokens available");
            return null;
        }

        // Parse "If" keyword
        Token ifToken = tokens.get(0);
        if (ifToken.getTokenType() != TokenType.ID_KEYWORD || !ifToken.getToken().equals("If")) {
            System.err.println("parseIfStmtNode: expected 'If', got '" + ifToken.getToken() + "' at " + ifToken.getFilename() + ":" + ifToken.getLineNum());
            return null;
        }
        tokens.remove(0);

        // Parse left bracket [
        if (tokens.size() == 0) {
            System.err.println("parseIfStmtNode: expected '[' after 'If'");
            return null;
        }
        Token leftBracket = tokens.get(0);
        if (leftBracket.getTokenType() != TokenType.L_BRACKET) {
            System.err.println("parseIfStmtNode: expected '[', got '" + leftBracket.getToken() + "' at " + leftBracket.getFilename() + ":" + leftBracket.getLineNum());
            return null;
        }
        tokens.remove(0);

        // Parse expression
        ExpressionNode condition = ExpressionNode.parseExpressionNode(tokens);
        if (condition == null) {
            System.err.println("parseIfStmtNode: failed to parse condition expression");
            return null;
        }

        // Parse right bracket ]
        if (tokens.size() == 0) {
            System.err.println("parseIfStmtNode: expected ']' after condition");
            return null;
        }
        Token rightBracket = tokens.get(0);
        if (rightBracket.getTokenType() != TokenType.R_BRACKET) {
            System.err.println("parseIfStmtNode: expected ']', got '" + rightBracket.getToken() + "' at " + rightBracket.getFilename() + ":" + rightBracket.getLineNum());
            return null;
        }
        tokens.remove(0);

        // Parse left brace {
        if (tokens.size() == 0) {
            System.err.println("parseIfStmtNode: expected '{' after condition");
            return null;
        }
        Token leftBrace = tokens.get(0);
        if (leftBrace.getTokenType() != TokenType.L_BRACE) {
            System.err.println("parseIfStmtNode: expected '{', got '" + leftBrace.getToken() + "' at " + leftBrace.getFilename() + ":" + leftBrace.getLineNum());
            return null;
        }
        tokens.remove(0);

        // Parse body
        BodyNode body = BodyNode.parseBodyNode(tokens);
        if (body == null) {
            System.err.println("parseIfStmtNode: failed to parse body");
            return null;
        }

        // Parse right brace }
        if (tokens.size() == 0) {
            System.err.println("parseIfStmtNode: expected '}' after body");
            return null;
        }
        Token rightBrace = tokens.get(0);
        if (rightBrace.getTokenType() != TokenType.R_BRACE) {
            System.err.println("parseIfStmtNode: expected '}', got '" + rightBrace.getToken() + "' at " + rightBrace.getFilename() + ":" + rightBrace.getLineNum());
            return null;
        }
        tokens.remove(0);

        // Parse zero or more ElseIf nodes (Kleene star)
        ArrayList<ElseIfNode> elseIfList = new ArrayList<>();
        while (tokens.size() > 0) {
            Token t = tokens.get(0);
            if (t.getTokenType() == TokenType.ID_KEYWORD && t.getToken().equals("Elseif")) {
                ElseIfNode elseIfNode = ElseIfNode.parseElseIfNode(tokens);
                if (elseIfNode == null) {
                    System.err.println("parseIfStmtNode: failed to parse elseif");
                    return null;
                }
                elseIfList.add(elseIfNode);
            } else {
                break;
            }
        }

        // Parse Else node (which can be epsilon)
        ElseNode elseNode = ElseNode.parseElseNode(tokens);
        if (elseNode == null) {
            System.err.println("parseIfStmtNode: failed to parse else");
            return null;
        }

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
