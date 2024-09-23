package io.lorde.ast.types;

import io.lorde.ast.AstVisitor;

public final class BaseType extends Type {
    public final String name;

    public BaseType(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
