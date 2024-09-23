package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public final class Identifier extends Expression {
    public String name;

    public Identifier(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
