package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class Grouping extends Expression {
    public Expression expression;

    public Grouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
