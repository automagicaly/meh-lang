package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class UnaryExpression extends Expression{
    public final UnaryOperator op;
    public final Expression expr;

    public UnaryExpression(UnaryOperator op, Expression expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
