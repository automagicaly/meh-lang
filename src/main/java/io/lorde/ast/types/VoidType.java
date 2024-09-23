package io.lorde.ast.types;

import io.lorde.ast.AstVisitor;

public final class VoidType extends Type{
    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
