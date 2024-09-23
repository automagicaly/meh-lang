package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class ShiftOperator extends BinaryOperator{
    public final String op;

    public ShiftOperator(String op) {
        this.op = op;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
