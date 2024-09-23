package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class Reference extends Expression{
    public final Identifier identifier;

    public Reference(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
