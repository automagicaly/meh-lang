package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;
import io.lorde.meh.ast.types.Type;

public final class Cast extends Expression {
    public Expression expression;
    public Type type;
    public final Token keyword;

    public Cast(Token keyword, Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
        this.keyword = keyword;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
