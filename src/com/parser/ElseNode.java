package parser;
import java.util.ArrayList;
import provided.*;

public class ElseNode implements JottTree{
    private final BodyNode bodyNode;
    private final boolean isEmpty; 

    public ElseNode(BodyNode bodyNode, boolean isEmpty) {
        this.bodyNode = bodyNode;
        this.isEmpty = isEmpty;
    }

    public static ElseNode parseElseNode(ArrayList<Token> tokens) {
        // Else { < body >} | ε
        
        if (tokens.isEmpty()){
			throw new ParseException("Unexpected EOF", null);
		}
		if (!tokens.get(0).getToken().equals("Else")) {
			throw new ParseException("Missing 'Else' Keyword", tokens.get(0));
		}

        
		// Check for opening brace ({)
		if (tokens.isEmpty()){
			throw new ParseException("Unexpected EOF", null);
		}
		if (!(tokens.get(0).getTokenType() == TokenType.L_BRACE)) {
			throw new ParseException("Missing '{' after 'Elseif'", tokens.get(0));
		}
		tokens.remove(0); // consume ({)

        if (tokens.get(0).getTokenType() == TokenType.R_BRACE) {
            tokens.remove(0); // consume (})
            return new ElseNode(null, true);
        }

		BodyNode body = BodyNode.parseBodyNode(tokens);

		// Check for closing brace (})
		if (tokens.isEmpty()){
			throw new ParseException("Unexpected EOF", null);
		}
		if (!(tokens.get(0).getTokenType() == TokenType.R_BRACE)) {
			throw new ParseException("Missing '}' to end Else body", tokens.get(0));
		}
		tokens.remove(0); // consume (})

        return new ElseNode(body, false);
    }

    @Override
    public String convertToJava(String indent) {
        // TODO: Implement conversion logic
        return "";
    }

    @Override
    public String convertToJott() {
        if (this.isEmpty) {return "";}
        else {
            return "Else {" + this.bodyNode.convertToJott() + "}";
        }
    }

    @Override
    public String convertToC() {
        // TODO: Implement conversion logic
        return "";
    }

    @Override
    public String convertToPython() {
        // TODO: Implement conversion logic
        return "";
    }

    @Override
    public boolean validateTree() {
        // TODO: Implement validation logic
        return true;
    }
}
