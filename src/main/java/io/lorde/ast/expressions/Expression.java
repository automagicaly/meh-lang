package io.lorde.ast.expressions;


import io.lorde.ast.*;

public abstract sealed class Expression permits BinaryExpression, Body, Cast, FunctionCall, Grouping, Identifier, IndexedIdentifier, IndexedReference, Lambda, Literal, Reference, TypeCheck, UnaryExpression {
    abstract public <T> T accept(AstVisitor<T> astVisitor);
}