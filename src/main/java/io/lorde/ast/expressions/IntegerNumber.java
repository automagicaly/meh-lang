package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class IntegerNumber extends Number {
    public int n;

    public IntegerNumber(int n) {
        this.n = n;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
