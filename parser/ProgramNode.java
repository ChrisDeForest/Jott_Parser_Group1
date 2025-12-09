package parser;

import provided.Token;
import provided.TokenType;
import provided.JottTree;
import java.util.ArrayList;

import execution.RuntimeEnv;

public class ProgramNode implements JottTree {
    private final ArrayList<FunctionDefNode> functionDefs;

    public ProgramNode(ArrayList<FunctionDefNode> functionDefs) {
        this.functionDefs = functionDefs;
    }

    public static ProgramNode parseProgramNode(ArrayList<Token> tokens) {
        ArrayList<FunctionDefNode> functionDefs = new ArrayList<>();

        // Parse zero or more function definitions (Kleene star)
        while (!tokens.isEmpty()) {
            Token t = tokens.get(0);

            if (t.getTokenType() == TokenType.ID_KEYWORD && (t.getToken().equals("Def") || t.getToken().equals("def"))) {
                // Will throw ParseException if it fails
                FunctionDefNode funcDef = FunctionDefNode.parseFunctionDefNode(tokens);
                functionDefs.add(funcDef);
            } else {
                break;
            }
        }

        // At this point, we should have consumed all tokens (EOF)
        if (!tokens.isEmpty()) {
            throw new ParseException("parseProgramNode: Unexpected tokens after function definitions: '"
                    + tokens.get(0).getToken() + "'", tokens.get(0));
        }

        return new ProgramNode(functionDefs);
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        for (FunctionDefNode funcDef : functionDefs) {
            sb.append(funcDef.convertToJott());
        }
        return sb.toString();
    }

    @Override
    public String convertToJava(String classname) {
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

    // in parser/ProgramNode.java
    @Override
    public boolean validateTree() {
        try {
            semantics.SymbolTable.reset();
            seedBuiltins();

            for (FunctionDefNode f : functionDefs) {
                // -- required tiny getters on FunctionDefNode/children --
                // public IDNode getId()
                // public FunctionDefParamsNode getParams()
                // public FunctionReturnNode getReturnType()
                // and on those:
                // IDNode: public String getName(); public provided.Token getToken();
                // FunctionDefParamsNode: public java.util.List<provided.Token>
                // getParamTypeTokens();
                // FunctionReturnNode: public provided.Token getReturnTypeToken(); public
                // boolean isVoid();

                String fname = f.getId().getName();
                provided.Token nameTok = f.getId().getToken();

                // params as Strings (what SymbolTable expects)
                java.util.List<String> paramTypes = new java.util.ArrayList<>();
                if (f.getParams() != null) {
                    for (provided.Token ptok : f.getParams().getParamTypeTokens()) {
                        String p = ptok.getToken();
                        // sanity: only known types for phase 3
                        if (!isKnownType(p)) {
                            throw new semantics.SemanticException("Unknown parameter type " + p, ptok);
                        }
                        paramTypes.add(p);
                    }
                }

                // return type as String
                String retType;
                if (f.getReturnType().isVoid()) {
                    retType = "Void";
                } else {
                    provided.Token rtok = f.getReturnType().getReturnTypeToken();
                    retType = rtok.getToken();
                    if (!isKnownType(retType)) {
                        throw new semantics.SemanticException("Unknown return type " + retType, rtok);
                    }
                }

                // add to global function table; guard duplicates
                boolean added = semantics.SymbolTable.addFunction(fname, retType, paramTypes);
                if (!added) {
                    throw new semantics.SemanticException("Duplicate function " + fname, nameTok);
                }
            }

            // enforce main[]:Void
            if (!semantics.SymbolTable.functionExists("main")) {
                throw new semantics.SemanticException("Missing main function", /* no precise token */ null);
            }
            if (!semantics.SymbolTable.hasMainFunction()) {
                // your tests require Void + zero params
                throw new semantics.SemanticException("main must be declared as main[]:Void", null);
            }

            // second pass: validate each function body
            boolean allOK = true;
            for (FunctionDefNode f : functionDefs) {
                // your teammates' FunctionDefNode.validateTree() should:
                // - enterScope()
                // - add params as initialized
                // - check body
                // - ensure returns (for non-Void)
                // - exitScope()
                boolean ok = f.validateTree();
                allOK = allOK && ok;
            }

            return allOK;
        } catch (semantics.SemanticException se) {
            // your SemanticException should already format:
            // Semantic Error
            // <message>
            // <filename>:<line>
            System.err.println(se.getMessage());
            return false;
        }
    }

    private static boolean isKnownType(String t) {
        return "Integer".equals(t) || "Double".equals(t) || "String".equals(t) ||
                "Boolean".equals(t) || "Void".equals(t);
    }

    private static void seedBuiltins() {
        // - No overloading support, so we model print as taking a single "Any" type.
        // - Your FunctionCallNode.validateTree() should treat "Any" as matching any
        // actual arg type.
        // - concat and length use concrete signatures.
        semantics.SymbolTable.addFunction("print", "Void", java.util.List.of("Any"));
        semantics.SymbolTable.addFunction("concat", "String", java.util.List.of("String", "String"));
        semantics.SymbolTable.addFunction("length", "Integer", java.util.List.of("String"));
    }

    public Object execute() {
        for (FunctionDefNode f : functionDefs) {
            f.execute(); 
        }

        if (RuntimeEnv.functionExists("main")) {
            return RuntimeEnv.callFunction("main", new ArrayList<>()); // no args
        } else {
            throw new RuntimeException("No main function defined");
        }
    }

}
