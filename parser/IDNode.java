package parser;

import provided.*;
import semantics.SemanticException;
import semantics.SymbolTable;
import semantics.SymbolTable.VariableInfo;

import java.util.ArrayList;
import semantics.*;

public class IDNode implements OperandNode {
    private final Token idToken;

    public IDNode(Token idToken) {
        this.idToken = idToken;
    }

    public static IDNode parseIDNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty()) throw new ParseException("parseIDNode: Unexpected EOF", null);
		
        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD) throw new ParseException("parseIDNode: Missing an ID Keyword", t);
        
        // consume the token and return a new IDNode
        tokens.remove(0);
        return new IDNode(t);
    }

    @Override
    public String getType(SymbolTable symbolTable) {
        // Look up the variable in the symbol table
        return symbolTable.getVariableType(idToken.getToken());
    }

    public String convertToJott() {
        // returns a string representation of the token/ID
        return idToken.getToken();
    }
    public String convertToJava(String classname) {
        return null;
    }
    public String convertToC() {
        return null;
    }
    public String convertToPython() {
        return null;
    }
    public boolean validateTree() {
        String varName = idToken.getToken();

        // check if var exists
        if (!SymbolTable.variableExists(varName)) {
            throw new SemanticException("parseIdNode: Variable '" + varName + "' is not declared.", idToken);
        }
        
        VariableInfo varInfo = SymbolTable.getVariable(varName);
         // check if var is initialized before use
        if (!varInfo.isInitialized()) {
            throw new SemanticException("parseIdNode: Variable '" + varName + "' is not initialized before use.", idToken);
        }

        return true;
    }
}
