package io.lorde.meh.ast.expressions;

import io.lorde.meh.ast.types.Type;

public final class TypeCheck extends Expression {
    public final Expression expression;
    public final Type type;

    public TypeCheck(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
