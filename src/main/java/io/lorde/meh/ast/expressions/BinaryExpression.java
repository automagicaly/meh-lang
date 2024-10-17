package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;

public final class BinaryExpression extends Expression {
    public Expression a;
    public Token operator;
    public Expression b;

    public BinaryExpression(Expression a, Token operator, Expression b) {
        this.a = a;
        this.operator = operator;
        this.b = b;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
