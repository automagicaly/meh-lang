package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class IndexedReference extends Expression{
    public final Reference reference;
    public final Expression index;

    public IndexedReference(Reference reference, Expression index) {
        this.reference = reference;
        this.index = index;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
