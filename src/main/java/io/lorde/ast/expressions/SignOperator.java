package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class SignOperator extends UnaryOperator{
    public final String op;

    public SignOperator(String op) {
        this.op = op;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
