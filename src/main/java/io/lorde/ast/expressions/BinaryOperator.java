package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

public abstract sealed class BinaryOperator permits BitWiseOperator, BooleanOperator, ComparisonOperator, NumericOperator, ShiftOperator {
    abstract public <T> T accept(AstVisitor<T> astVisitor);
}
