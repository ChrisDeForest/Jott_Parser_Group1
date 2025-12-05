package parser;

import provided.*;
import semantics.*;

import java.util.ArrayList;

public class IDNode implements OperandNode {
    private final Token idToken;

    public IDNode(Token idToken) {
        this.idToken = idToken;
    }

    public static IDNode parseIDNode(ArrayList<Token> tokens) {
        if (tokens.isEmpty())
            throw new ParseException("parseIDNode: Unexpected EOF", null);

        Token t = tokens.get(0);
        if (t.getTokenType() != TokenType.ID_KEYWORD)
            throw new ParseException("parseIDNode: Missing an ID Keyword", t);

        // consume the token and return a new IDNode
        tokens.remove(0);
        return new IDNode(t);
    }

    @Override
    public String getType(SymbolTable symbolTable) {
        // Look up the variable in the symbol table
        return symbolTable.getVariableType(idToken.getToken());
    }

    public provided.Token getToken() {
        return idToken;
    }

    public String getName() {
        // returns string name of ID
        return idToken.getToken();
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
        return true;
    }

    @Override
    public Object evaluate(){
         String varName = idToken.getToken();

        // Look up the variable in the runtime environment
        if (!execution.RuntimeEnv.variableExists(varName)) {
            throw new RuntimeException("IDNode: Variable '" + varName + "' not initialized at runtime.");
        }

        return execution.RuntimeEnv.getVariable(varName);
    }
}
