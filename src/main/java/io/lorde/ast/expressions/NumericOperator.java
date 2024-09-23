package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class NumericOperator extends BinaryOperator{
    public final String op;

    public NumericOperator(String op) {
        this.op = op;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
