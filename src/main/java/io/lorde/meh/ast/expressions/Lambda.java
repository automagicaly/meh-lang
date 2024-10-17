package io.lorde.meh.ast.expressions;

import java.util.List;

public final class Lambda extends Expression {
    public List<FunctionParameter> parameters;
    public Expression body;

    public Lambda(List<FunctionParameter> parameters, Expression body) {
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
