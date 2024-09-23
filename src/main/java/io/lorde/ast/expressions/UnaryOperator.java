package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public abstract sealed class UnaryOperator permits NotOperator, SignOperator {
    abstract public <T> T accept(AstVisitor<T> astVisitor);
}
