package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;

public final class UnaryExpression extends Expression{
    public final Token op;
    public final Expression expr;

    public UnaryExpression(Token op, Expression expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
