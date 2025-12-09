package parser;

import java.util.ArrayList;
import java.util.List;
import execution.*;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import semantics.SymbolTable;

public class FunctionDefNode implements JottTree {
    private final Token defToken;
    private final IDNode functionId;
    private final FunctionDefParamsNode params;
    private final FunctionReturnNode returnType;
    private final FBodyNode body;

    public FunctionDefNode(Token defToken, IDNode functionId, FunctionDefParamsNode params, FunctionReturnNode returnType,
            FBodyNode body) {
        this.defToken = defToken;
        this.functionId = functionId;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public static FunctionDefNode parseFunctionDefNode(ArrayList<Token> tokens) {
        // <function_def> -> Def <id> [function_def_params]:<function_return>{<f_body>}

        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionDefNode: Unexpected EOF while parsing function definition", null);
        }

        // Check for "Def" keyword
        Token defToken = tokens.get(0);
        if (defToken.getTokenType() != TokenType.ID_KEYWORD || (!defToken.getToken().equals("Def") && !defToken.getToken().equals("def"))) {
            throw new ParseException("parseFunctionDefNode: Expected 'Def' keyword, got '" + defToken.getToken() + "'",
                    defToken);
        }
        tokens.remove(0); // consume "Def"

        // Parse function ID
        IDNode functionId = IDNode.parseIDNode(tokens);

        // Check for opening bracket '['
        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionDefNode: Unexpected EOF after function name", null);
        }
        Token openBracket = tokens.get(0);
        if (openBracket.getTokenType() != TokenType.L_BRACKET) {
            throw new ParseException(
                    "parseFunctionDefNode: Expected '[' after function name, got '" + openBracket.getToken() + "'",
                    openBracket);
        }
        tokens.remove(0); // consume '['

        // Parse function parameters
        FunctionDefParamsNode params = FunctionDefParamsNode.parseFunctionDefParamsNode(tokens);

        // Check for closing bracket ']'
        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionDefNode: Unexpected EOF after function parameters", null);
        }
        Token closeBracket = tokens.get(0);
        if (closeBracket.getTokenType() != TokenType.R_BRACKET) {
            throw new ParseException("parseFunctionDefNode: Expected ']' after function parameters, got '"
                    + closeBracket.getToken() + "'", closeBracket);
        }
        tokens.remove(0); // consume ']'

        // Check for colon ':'
        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionDefNode: Unexpected EOF after function parameters", null);
        }
        Token colon = tokens.get(0);
        if (colon.getTokenType() != TokenType.COLON) {
            throw new ParseException(
                    "parseFunctionDefNode: Expected ':' after function parameters, got '" + colon.getToken() + "'",
                    colon);
        }
        tokens.remove(0); // consume ':'

        // Parse return type
        FunctionReturnNode returnType = FunctionReturnNode.parseFunctionReturnNode(tokens);

        // Check for opening brace '{'
        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionDefNode: Unexpected EOF after function return type", null);
        }
        Token openBrace = tokens.get(0);
        if (openBrace.getTokenType() != TokenType.L_BRACE) {
            throw new ParseException(
                    "parseFunctionDefNode: Expected '{' after function return type, got '" + openBrace.getToken() + "'",
                    openBrace);
        }
        tokens.remove(0); // consume '{'

        // Parse function body
        FBodyNode body = FBodyNode.parseFBodyNode(tokens);

        // Check for closing brace '}'
        if (tokens.isEmpty()) {
            throw new ParseException("parseFunctionDefNode: Unexpected EOF after function body", null);
        }
        Token closeBrace = tokens.get(0);
        if (closeBrace.getTokenType() != TokenType.R_BRACE) {
            throw new ParseException(
                    "parseFunctionDefNode: Expected '}' after function body, got '" + closeBrace.getToken() + "'",
                    closeBrace);
        }
        tokens.remove(0); // consume '}'

        return new FunctionDefNode(defToken, functionId, params, returnType, body);
    }

    public IDNode getId() {
        return this.functionId;
    }

    public FunctionDefParamsNode getParams() {
        return this.params;
    }

    public FunctionReturnNode getReturnType() {
        return this.returnType;
    }

    public FBodyNode getBody() {
        return this.body;
    }

    @Override
    public String convertToJott() {
        return this.defToken.getToken() + " " + this.functionId.convertToJott() + "[" + this.params.convertToJott() + "]:" + this.returnType.convertToJott()
                + "{" + body.convertToJott() + "}";
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
        boolean ok = true;

        // 1) DO NOT validate the function identifier as a variable.
        // It's a function name, not a var use.
        // functionId.validateTree(); // <-- remove this line

        // These two are fine: they check syntax/types (not symbol lookups).
        if (params != null)
            ok &= params.validateTree();
        ok &= returnType.validateTree();

        // 2) New scope for the function body (so params live inside it)
        SymbolTable.enterScope();
            // 3) Add params to the current scope as initialized variables.
            // You need names AND types. Adapt the accessor to whatever you have.
            // Example API — adjust to your real methods:
            // params.getParamEntries() -> List<ParamEntry>
            // ParamEntry#getNameToken(), #getTypeToken()
            if (params != null) {
                for (var p : params.getParamEntries()) {
                    String pname = p.getNameToken().getToken();
                    String ptype = p.getTypeToken().getToken();

                    // declare
                    if (!SymbolTable.addVariable(pname, ptype)) {
                        throw new semantics.SemanticException(
                                "Duplicate parameter " + pname, p.getNameToken());
                    }
                    // mark initialized so it can be used immediately
                    SymbolTable.initializeVariable(pname);
                }
            }

            // 4) Validate the body with awareness of the function’s return type.
            // Ideally your FBodyNode has a validateTree(expectedReturnType) that
            // checks "all paths return" when expectedReturnType != "Void".
            String expectedReturn = returnType.isVoid() ? "Void" : returnType.getReturnTypeToken().getToken();

        ok &= body.validateTree(expectedReturn);

        // 5) Always leave the function scope
        SymbolTable.exitScope();

        return ok;
    }

    public Object execute() {
        String funcName = functionId.getName();

        // Register this function in the runtime environment
        RuntimeEnv.addFunction(funcName, new RuntimeEnv.JottFunction() {

            @Override
            public Object invoke(RuntimeEnv env, List<Object> args) {

                RuntimeEnv.enterScope();
                if (params != null) {
                    List<FunctionDefParamsNode.ParamDecl> paramDecls = params.getParamEntries();
                    for (int i = 0; i < paramDecls.size(); i++) {
                        String pname = paramDecls.get(i).getNameToken().getToken();
                        String ptype = paramDecls.get(i).getTypeToken().getToken();
                        Object argVal = args.get(i);

                        RuntimeEnv.declareVariable(pname, ptype, argVal);
                    }
                }

                Object result = body.execute();

                execution.RuntimeEnv.exitScope();

                if (returnType.isVoid()){
                    return null;
                }
                else {
                    return result;
                }
            }
        });

        return null;
    }   
}
