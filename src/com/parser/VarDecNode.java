package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.ParseException;
import java.util.ArrayList;

public class VarDecNode implements JottTree {

    private final TypeNode typeNode;
    private final IDNode idNode;
    
    public VarDecNode(TypeNode typeNode, IDNode idNode) {
        this.typeNode = typeNode;
        this.idNode = idNode;
    }

    public static VarDecNode parseVarDecNode(ArrayList<Token> tokens) {
        // < var_dec > -> < type > < id >;
        
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF while parsing variable declaration", null);
        }
        
        // Parse the type
        TypeNode typeNode = TypeNode.parseTypeNode(tokens);
        
        // Parse the ID
        IDNode idNode = IDNode.parseIDNode(tokens);
        
        // Check for semicolon
        if (tokens.isEmpty()) {
            throw new ParseException("Expected semicolon after variable declaration", null);
        }
        
        Token semicolon = tokens.get(0);
        if (semicolon.getTokenType() != TokenType.SEMICOLON) {
            throw new ParseException("Expected semicolon after variable declaration, got '" + semicolon.getToken() + "'", semicolon);
        }
        
        // Consume the semicolon
        tokens.remove(0);
        
        return new VarDecNode(typeNode, idNode);
    }

    @Override
    public String convertToJava(String indent) {
        // TODO: Implement conversion logic
        return "";
    }

    @Override
    public boolean validateTree() {
        // TODO: Implement validation logic
        return true;
    }

    @Override
    public String convertToJott() {
        return typeNode.convertToJott() + " " + idNode.convertToJott() + ";";
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
    
}
