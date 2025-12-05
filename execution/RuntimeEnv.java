package execution;

import java.util.*;

/**
 * RuntimeEnv manages variable values and function execution at runtime.
 * Supports scoping for nested scopes (e.g., function calls, loops).
 */


public class RuntimeEnv {

    // Stack of scopes, where each scope is a map of variable name -> value
    private final Deque<Map<String, Object>> variableScopes;

    // Global function table (functions are not scoped)
    private final Map<String, JottFunction> functions;

    // 🔑 Global static instance (like SymbolTable.globalSymbolTable)
    public static final RuntimeEnv globalRuntimeEnv = new RuntimeEnv();

    private RuntimeEnv() {
        this.variableScopes = new ArrayDeque<>();
        this.functions = new HashMap<>();
        // Start with a global scope
        this.variableScopes.push(new HashMap<>());
    }

    /** Enter a new scope (e.g., when entering a function body). */
    public static void enterScope() {
        globalRuntimeEnv.variableScopes.push(new HashMap<>());
    }

    /** Exit the current scope (e.g., when leaving a function body). */
    public static void exitScope() {
        if (globalRuntimeEnv.variableScopes.size() <= 1) {
            throw new IllegalStateException("Cannot exit the global scope");
        }
        globalRuntimeEnv.variableScopes.pop();
    }

    /** Reset runtime environment (clear all variables and functions). */
    public static void reset() {
        globalRuntimeEnv.functions.clear();
        globalRuntimeEnv.variableScopes.clear();
        globalRuntimeEnv.variableScopes.push(new HashMap<>());
        seedBuiltins();
    }

    /** Add or update a variable in the current scope. */
    public static void setVariable(String name, Object value) {
        globalRuntimeEnv.variableScopes.peek().put(name, value);
    }

    /** Check if a variable exists in any accessible scope. */
    public static boolean variableExists(String name) {
        for (Map<String, Object> scope : globalRuntimeEnv.variableScopes) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    /** Get the value of a variable from the most recent accessible scope. */
    public static Object getVariable(String name) {
        for (Map<String, Object> scope : globalRuntimeEnv.variableScopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null; // undeclared at runtime
    }

    /** Add a function to the global function table. */
    public static void addFunction(String name, JottFunction function) {
        globalRuntimeEnv.functions.put(name, function);
    }

    /** Check if a function exists. */
    public static boolean functionExists(String name) {
        return globalRuntimeEnv.functions.containsKey(name);
    }

    /** Call a function by name with arguments. */
    public static Object callFunction(String name, List<Object> args) {
        JottFunction f = globalRuntimeEnv.functions.get(name);
        if (f == null) {
            throw new RuntimeException("Function '" + name + "' not found at runtime.");
        }
        return f.invoke(globalRuntimeEnv, args);
    }

    /** Functional interface for user-defined or built-in functions. */
    @FunctionalInterface
    public interface JottFunction {
        Object invoke(RuntimeEnv env, List<Object> args);
    }

    /** Seed built-in functions into the runtime environment. */
    private static void seedBuiltins() {
        // print[Any]:Void
        addFunction("print", (env, args) -> {
            if (!args.isEmpty()) {
                System.out.println(args.get(0));
            }
            return null;
        });

        // concat[String, String]:String
        addFunction("concat", (env, args) -> {
            if (args.size() == 2) {
                return String.valueOf(args.get(0)) + String.valueOf(args.get(1));
            }
            throw new RuntimeException("concat expects 2 string arguments");
        });

        // length[String]:Integer
        addFunction("length", (env, args) -> {
            if (args.size() == 1) {
                return String.valueOf(args.get(0)).length();
            }
            throw new RuntimeException("length expects 1 string argument");
        });
    }
}