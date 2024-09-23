package io.lorde.ast.types;

import io.lorde.ast.AstVisitor;

public final class ArrayType extends Type {
    public final Type type;

    public ArrayType(Type type) {
        this.type = type;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
