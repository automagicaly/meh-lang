package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;

public final class Reference extends Expression{
    public final Token identifier;

    public Reference(Token identifier) {
        this.identifier = identifier;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
