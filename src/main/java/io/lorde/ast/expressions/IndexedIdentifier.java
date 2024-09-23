package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class IndexedIdentifier extends Expression {
    public Identifier identifier;
    public Expression index;

    public IndexedIdentifier(Identifier identifier, Expression index) {
        this.identifier = identifier;
        this.index = index;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
