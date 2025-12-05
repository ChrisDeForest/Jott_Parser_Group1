package execution;

import java.util.*;

/**
 * RuntimeEnv manages variable values and function execution at runtime.
 * Supports scoping for nested scopes (e.g., function calls, loops).
 */
public class RuntimeEnv {

    /**
     * VariableRecord holds both the declared type and the current value.
     */
    public static class VariableRecord {
        private final String type; // "Integer", "Double", "String", "Boolean"
        private Object value;

        public VariableRecord(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        public String getType() { return type; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
    }

    // Stack of scopes, where each scope is a map of variable name -> VariableRecord
    private final Deque<Map<String, VariableRecord>> variableScopes;

    // Global function table (functions are not scoped)
    private final Map<String, JottFunction> functions;

    // 🔑 Global static instance
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

    /** Declare a variable in the current scope with type and initial value. */
    public static void declareVariable(String name, String type, Object value) {
        globalRuntimeEnv.variableScopes.peek().put(name, new VariableRecord(type, value));
    }

    /** Update an existing variable’s value. */
    public static void setVariable(String name, Object value) {
        for (Map<String, VariableRecord> scope : globalRuntimeEnv.variableScopes) {
            if (scope.containsKey(name)) {
                scope.get(name).setValue(value);
                return;
            }
        }
        throw new RuntimeException("Variable '" + name + "' not declared");
    }

    /** Check if a variable exists in any accessible scope. */
    public static boolean variableExists(String name) {
        for (Map<String, VariableRecord> scope : globalRuntimeEnv.variableScopes) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    /** Get the value of a variable from the most recent accessible scope. */
    public static Object getVariable(String name) {
        for (Map<String, VariableRecord> scope : globalRuntimeEnv.variableScopes) {
            if (scope.containsKey(name)) {
                return scope.get(name).getValue();
            }
        }
        return null; // undeclared at runtime
    }

    /** Get the type of a variable from the most recent accessible scope. */
    public static String getVariableType(String name) {
        for (Map<String, VariableRecord> scope : globalRuntimeEnv.variableScopes) {
            if (scope.containsKey(name)) {
                return scope.get(name).getType();
            }
        }
        return null;
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
                System.out.println("Arg" + args.get(0) + "Arg length: " + String.valueOf(args.get(0)).length());
                return String.valueOf(args.get(0)).length();
            }
            throw new RuntimeException("length expects 1 string argument");
        });
    }
}