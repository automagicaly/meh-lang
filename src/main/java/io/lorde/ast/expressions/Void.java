package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class Void extends Literal{
    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
