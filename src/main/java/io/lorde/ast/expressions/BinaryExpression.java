package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class BinaryExpression extends Expression {
    public Expression a;
    public BinaryOperator operator;
    public Expression b;

    public BinaryExpression(Expression a, BinaryOperator operator, Expression b) {
        this.a = a;
        this.operator = operator;
        this.b = b;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
