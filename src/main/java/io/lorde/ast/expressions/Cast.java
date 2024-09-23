package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.types.Type;

public final class Cast extends Expression {
    public Expression expression;
    public Type type;

    public Cast(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
