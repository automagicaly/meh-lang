package io.lorde.meh.ast.expressions;

public final class Grouping extends Expression {
    public Expression expression;

    public Grouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
