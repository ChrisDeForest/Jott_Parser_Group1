package parser;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import provided.ParseException;

public class FunctionDefNode implements JottTree {
    private final IDNode functionId;
    private final FunctionDefParamsNode params;
    private final FunctionReturnNode returnType;
    private final FBodyNode body;
    
    public FunctionDefNode(IDNode functionId, FunctionDefParamsNode params, FunctionReturnNode returnType, FBodyNode body) {
        this.functionId = functionId;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public static FunctionDefNode parseFunctionDefNode(ArrayList<Token> tokens) {
        // <function_def> -> Def <id> [function_def_params]:<function_return>{<f_body>}
        
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF while parsing function definition", null);
        }
        
        // Check for "Def" keyword
        Token defToken = tokens.get(0);
        if (defToken.getTokenType() != TokenType.ID_KEYWORD || !defToken.getToken().equals("Def")) {
            throw new ParseException("Expected 'Def' keyword, got '" + defToken.getToken() + "'", defToken);
        }
        tokens.remove(0); // consume "Def"
        
        // Parse function ID
        IDNode functionId = IDNode.parseIDNode(tokens);
        
        // Check for opening bracket '['
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF after function name", null);
        }
        Token openBracket = tokens.get(0);
        if (openBracket.getTokenType() != TokenType.L_BRACKET) {
            throw new ParseException("Expected '[' after function name, got '" + openBracket.getToken() + "'", openBracket);
        }
        tokens.remove(0); // consume '['
        
        // Parse function parameters
        FunctionDefParamsNode params = FunctionDefParamsNode.parseFunctionDefParamsNode(tokens);
        
        // Check for closing bracket ']'
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF after function parameters", null);
        }
        Token closeBracket = tokens.get(0);
        if (closeBracket.getTokenType() != TokenType.R_BRACKET) {
            throw new ParseException("Expected ']' after function parameters, got '" + closeBracket.getToken() + "'", closeBracket);
        }
        tokens.remove(0); // consume ']'
        
        // Check for colon ':'
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF after function parameters", null);
        }
        Token colon = tokens.get(0);
        if (colon.getTokenType() != TokenType.COLON) {
            throw new ParseException("Expected ':' after function parameters, got '" + colon.getToken() + "'", colon);
        }
        tokens.remove(0); // consume ':'
        
        // Parse return type
        FunctionReturnNode returnType = FunctionReturnNode.parseFunctionReturnNode(tokens);
        
        // Check for opening brace '{'
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF after function return type", null);
        }
        Token openBrace = tokens.get(0);
        if (openBrace.getTokenType() != TokenType.L_BRACE) {
            throw new ParseException("Expected '{' after function return type, got '" + openBrace.getToken() + "'", openBrace);
        }
        tokens.remove(0); // consume '{'
        
        // Parse function body
        FBodyNode body = FBodyNode.parseFBodyNode(tokens);
        
        // Check for closing brace '}'
        if (tokens.isEmpty()) {
            throw new ParseException("Unexpected EOF after function body", null);
        }
        Token closeBrace = tokens.get(0);
        if (closeBrace.getTokenType() != TokenType.R_BRACE) {
            throw new ParseException("Expected '}' after function body, got '" + closeBrace.getToken() + "'", closeBrace);
        }
        tokens.remove(0); // consume '}'
        
        return new FunctionDefNode(functionId, params, returnType, body);
    }

    @Override
    public String convertToJott() {
        return "Def " + functionId.convertToJott() + "[" + params.convertToJott() + "]:" + returnType.convertToJott() + "{" + body.convertToJott() + "}";
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
