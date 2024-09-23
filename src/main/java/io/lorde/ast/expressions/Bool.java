package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class Bool extends Literal {
    public boolean state;

    public Bool(boolean state) {
        this.state = state;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
