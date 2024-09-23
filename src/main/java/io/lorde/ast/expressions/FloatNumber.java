package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class FloatNumber extends Number {
    public float n;

    public FloatNumber(float n) {
        this.n = n;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
