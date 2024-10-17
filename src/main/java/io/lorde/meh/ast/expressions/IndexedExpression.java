package io.lorde.meh.ast.expressions;

public final class IndexedExpression extends Expression{
    public final Expression expr;
    public final Expression index;

    public IndexedExpression(Expression expr, Expression index) {
        this.expr = expr;
        this.index = index;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
