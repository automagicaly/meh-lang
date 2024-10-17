package io.lorde.meh.ast.statements;

import io.lorde.meh.ast.expressions.Expression;

public final class Return extends Statement {
    public final Expression expression;

    public Return(Expression expression) {
        if (expression == null) {
            throw new RuntimeException("Return requires an expression.");
        }
        this.expression = expression;
    }

    @Override
    public <T> T accept(StatementVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
