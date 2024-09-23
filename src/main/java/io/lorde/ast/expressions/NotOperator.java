package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class NotOperator extends UnaryOperator {
    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
