package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class DoubleNumber extends Number {
    public double n;

    public DoubleNumber(double n) {
        this.n = n;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
