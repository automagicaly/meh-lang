package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class StringLiteral extends Literal {
    public String str;

    public StringLiteral(java.lang.String str) {
        this.str = str;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
