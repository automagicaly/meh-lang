package io.lorde.ast.statements;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.expressions.Expression;

public final class ExpressionEvaluation extends Statement{
    public final Expression expression;

    public ExpressionEvaluation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
