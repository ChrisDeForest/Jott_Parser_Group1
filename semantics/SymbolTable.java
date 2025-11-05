package semantics;

import java.util.*;

/**
 * SymbolTable manages variable and function definitions with their types.
 * Supports scoping for nested scopes (e.g., function bodies).
 */
public class SymbolTable {
    
    /**
     * Information about a variable: its type and whether it's been initialized.
     */
    public static class VariableInfo {
        private final String type;  // "Double", "Integer", "String", "Boolean"
        private boolean initialized;
        
        public VariableInfo(String type) {
            this.type = type;
            this.initialized = false;
        }
        
        public String getType() {
            return type;
        }
        
        public boolean isInitialized() {
            return initialized;
        }
        
        public void setInitialized(boolean initialized) {
            this.initialized = initialized;
        }
    }
    
    /**
     * Information about a function: return type and parameter types.
     */
    public static class FunctionInfo {
        private final String returnType;  // "Double", "Integer", "String", "Boolean", or "Void"
        private final List<String> paramTypes;  // List of parameter types in order
        
        public FunctionInfo(String returnType, List<String> paramTypes) {
            this.returnType = returnType;
            this.paramTypes = new ArrayList<>(paramTypes);
        }
        
        public String getReturnType() {
            return returnType;
        }
        
        public List<String> getParamTypes() {
            return new ArrayList<>(paramTypes);  // Return a copy to prevent modification
        }
        
        public int getParamCount() {
            return paramTypes.size();
        }
    }
    
    // Stack of scopes, where each scope is a map of variable name -> VariableInfo
    private final Stack<Map<String, VariableInfo>> variableScopes;
    
    // Global function table (functions are not scoped)
    private final Map<String, FunctionInfo> functions;

    public static final SymbolTable globalSymbolTable = new SymbolTable();
    
    public SymbolTable() {
        this.variableScopes = new Stack<>();
        this.functions = new HashMap<>();
        // Start with a global scope
        enterScope();
    }
    
    /**
     * Enter a new scope (e.g., when entering a function body).
     */
    public static void enterScope() {
        globalSymbolTable.variableScopes.push(new HashMap<>());
    }
    
    /**
     * Exit the current scope (e.g., when leaving a function body).
     */
    public static void exitScope() {
        if (globalSymbolTable.variableScopes.size() <= 1) {
            throw new IllegalStateException("Cannot exit the global scope");
        }
        globalSymbolTable.variableScopes.pop();
    }
    
    /**
     * Add a variable to the current scope.
     * @param name The variable name
     * @param type The variable type ("Double", "Integer", "String", "Boolean")
     * @return true if added successfully, false if variable already exists in current scope
     */
    public static boolean addVariable(String name, String type) {
        Map<String, VariableInfo> currentScope = globalSymbolTable.variableScopes.peek();
        if (currentScope.containsKey(name)) {
            return false;  // Variable already declared in this scope
        }
        currentScope.put(name, new VariableInfo(type));
        return true;
    }
    
    /**
     * Check if a variable exists in any accessible scope (searches from current scope up).
     * @param name The variable name
     * @return true if the variable exists, false otherwise
     */
    public static boolean variableExists(String name) {
        // Search from current scope up to global scope
        for (int i = globalSymbolTable.variableScopes.size() - 1; i >= 0; i--) {
            if (globalSymbolTable.variableScopes.get(i).containsKey(name)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get variable information from the most recent accessible scope.
     * @param name The variable name
     * @return VariableInfo if found, null otherwise
     */
    public static VariableInfo getVariable(String name) {
        // Search from current scope up to global scope
        for (int i = globalSymbolTable.variableScopes.size() - 1; i >= 0; i--) {
            VariableInfo info = globalSymbolTable.variableScopes.get(i).get(name);
            if (info != null) {
                return info;
            }
        }
        return null;
    }
    
    /**
     * Get the type of a variable.
     * @param name The variable name
     * @return The type string if found, null otherwise
     */
    public static String getVariableType(String name) {
        VariableInfo info = getVariable(name);
        return info != null ? info.getType() : null;
    }
    
    /**
     * Mark a variable as initialized.
     * @param name The variable name
     * @return true if variable was found and marked, false otherwise
     */
    public static boolean initializeVariable(String name) {
        VariableInfo info = getVariable(name);
        if (info != null) {
            info.setInitialized(true);
            return true;
        }
        return false;
    }
    
    /**
     * Add a function to the global function table.
     * @param name The function name
     * @param returnType The return type ("Double", "Integer", "String", "Boolean", or "Void")
     * @param paramTypes List of parameter types in order
     * @return true if added successfully, false if function already exists
     */
    public static boolean addFunction(String name, String returnType, List<String> paramTypes) {
        if (globalSymbolTable.functions.containsKey(name)) {
            return false;  // Function already defined
        }
        globalSymbolTable.functions.put(name, new FunctionInfo(returnType, paramTypes));
        return true;
    }
    
    /**
     * Check if a function exists.
     * @param name The function name
     * @return true if the function exists, false otherwise
     */
    public static boolean functionExists(String name) {
        return globalSymbolTable.functions.containsKey(name);
    }
    
    /**
     * Get function information.
     * @param name The function name
     * @return FunctionInfo if found, null otherwise
     */
    public static FunctionInfo getFunction(String name) {
        return globalSymbolTable.functions.get(name);
    }
    
    /**
     * Get the return type of a function.
     * @param name The function name
     * @return The return type string if found, null otherwise
     */
    public static String getFunctionReturnType(String name) {
        FunctionInfo info = globalSymbolTable.functions.get(name);
        return info != null ? globalSymbolTable.functions.get(name).getReturnType() : null;
    }
    
    /**
     * Check if a function has a main function with signature: main[]:Void
     * @return true if main function exists with correct signature
     */
    public static boolean hasMainFunction() {
        FunctionInfo main = globalSymbolTable.functions.get("main");
        if (main == null) {
            return false;
        }
        // Check: return type is Void and no parameters
        return "Void".equals(main.getReturnType()) && main.getParamCount() == 0;
    }
}

