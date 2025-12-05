package parser;

import java.util.ArrayList;
import provided.*;

public class ElseNode implements JottTree {
    private final BodyNode bodyNode;
    private final boolean isEmpty;

    public ElseNode(BodyNode bodyNode, boolean isEmpty) {
        this.bodyNode = bodyNode;
        this.isEmpty = isEmpty;
    }

    public static ElseNode parseElseNode(ArrayList<Token> tokens) {
        // Else { < body > } | ε
        if (tokens.isEmpty()) {
            return new ElseNode(null, true); // epsilon case
        }

        // Check if next token is "Else"
        if (!tokens.get(0).getToken().equals("Else")) {
            return new ElseNode(null, true); // epsilon case - no Else clause
        }

        // Consume "Else" token
        tokens.remove(0);

        // Check for opening brace ({)
        if (tokens.isEmpty()) {
            throw new ParseException("parseElseNode: Unexpected EOF", null);
        }
        if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            throw new ParseException("parseElseNode: Missing '{' after 'Else'", tokens.get(0));
        }
        tokens.remove(0); // consume ({)

        // Allow empty else block: Else { }
        if (tokens.isEmpty()) {
            throw new ParseException("parseElseNode: Unexpected EOF", null);
        }
        if (tokens.get(0).getTokenType() == TokenType.R_BRACE) {
            tokens.remove(0); // consume (})
            return new ElseNode(null, true);
        }

        BodyNode body = BodyNode.parseBodyNode(tokens);

        // Check for closing brace (})
        if (tokens.isEmpty()) {
            throw new ParseException("parseElseNode: Unexpected EOF", null);
        }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            throw new ParseException("parseElseNode: Missing '}' to end Else body", tokens.get(0));
        }
        tokens.remove(0); // consume (})

        return new ElseNode(body, false);
    }

    @Override
    public String convertToJava(String indent) {
        return "";
    }

    @Override
    public String convertToJott() {
        if (this.isEmpty) {
            return "";
        }
        return "Else {" + this.bodyNode.convertToJott() + "}";
    }

    @Override
    public String convertToC() {
        return "";
    }

    @Override
    public String convertToPython() {
        return "";
    }

    @Override
    public boolean validateTree() {
        if (this.isEmpty) {
            return true;
        }
        // validate body with special marker to skip function-level return validation
        bodyNode.validateTree("__IF_STATEMENT_BODY__");
        return true;
    }

    /** true if an Else block is actually present (not epsilon). */
    public boolean isPresent() {
        return !isEmpty;
    }

    /** returns the Else body (may be null if isEmpty). */
    public BodyNode getBody() {
        return bodyNode;
    }

    public Object evaluate(){
        return null;
    }
}
