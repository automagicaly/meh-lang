package io.lorde.ast.types;

import io.lorde.ast.AstVisitor;

public final class AnyType extends Type {
    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}