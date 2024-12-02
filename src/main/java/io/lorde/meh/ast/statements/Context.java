package io.lorde.meh.ast.statements;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Context parentContext;
    private final Map<String, Object> symbolTable;
    public int indentation;

    public Context(Context parentContext, int indentation) {
        this.parentContext = parentContext;
        this.symbolTable = new HashMap<>();
        this.indentation = indentation;
    }

    public Object get(String name) {
        if (this.symbolTable.containsKey(name)) {
            return this.symbolTable.get(name);
        }

        if (this.parentContext != null) {
            return this.parentContext.get(name);
        }

        return null;
    }

    public void set(String name, Object value) {
        if (this.symbolTable.containsKey(name)) {
            throw new RuntimeException("Symbol '%s' already defined on current context".formatted(name));
        }
        this.symbolTable.put(name, value);
    }
}
