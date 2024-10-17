package io.lorde.meh.ast.expressions;


import io.lorde.meh.ast.statements.Body;

public abstract sealed class Expression permits BinaryExpression, Cast, FunctionCall, Grouping, IndexedExpression, Lambda, ListLiteral, Literal, Reference, TypeCheck, UnaryExpression {
    abstract public <T> T accept(ExpressionVisitor<T> visitor);
}