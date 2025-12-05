package parser;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import semantics.*;

import java.util.ArrayList;

import execution.RuntimeEnv;

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
            throw new ParseException("parseVarDecNode: Unexpected EOF while parsing variable declaration", null);
        }
        
        // Parse the type
        TypeNode typeNode = TypeNode.parseTypeNode(tokens);
        
        // Parse the ID
        IDNode idNode = IDNode.parseIDNode(tokens);
        
        // Check for semicolon
        if (tokens.isEmpty()) {
            throw new ParseException("parseVarDecNode: Expected semicolon after variable declaration", null);
        }
        
        Token semicolon = tokens.get(0);
        if (semicolon.getTokenType() != TokenType.SEMICOLON) {
            throw new ParseException("parseVarDecNode: Expected semicolon after variable declaration, got '" + semicolon.getToken() + "'", semicolon);
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
        // - Add variable to symbol table
        // - Check for duplicate declaration in current scope
        typeNode.validateTree(); // this will throw error if false
        idNode.validateTree();
        
        String type = typeNode.getType();
        String name = idNode.getName();

        if(name.equals("While")){
            throw new SemanticException("VarDecNode: While is keyword, cannot be used as id", null);
        }

        boolean add_success = SymbolTable.addVariable(name, type); // will return false if variable already exists in current scope
        if (!add_success) {
            throw new SemanticException("VarDecNode: Variable '" + name + "' is already declared in the current scope.", null);
        }

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
    
   
    @Override
    public Object evaluate() {
        String name = idNode.getName();
        String type = typeNode.getType();

        // Declare the variable with its type and an initial null value
        RuntimeEnv.declareVariable(name, type, null);

        return null;
    }
}
