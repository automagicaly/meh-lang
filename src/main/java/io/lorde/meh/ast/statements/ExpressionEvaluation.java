package io.lorde.meh.ast.statements;

import io.lorde.meh.ast.expressions.Expression;

public final class ExpressionEvaluation extends Statement{
    public final Expression expression;

    public ExpressionEvaluation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(StatementVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
