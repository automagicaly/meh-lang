package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class BitWiseOperator extends BinaryOperator{
    public final String op;

    public BitWiseOperator(String op) {
        this.op = op;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
