package io.lorde.meh.ast.expressions;

public interface ExpressionVisitor<T> {
    T visit(BinaryExpression binaryExpression);
    T visit(Cast cast);
    T visit(FunctionCall functionCall);
    T visit(Grouping grouping);
    T visit(IndexedExpression indexedExpression);
    T visit(Lambda lambda);
    T visit(ListLiteral listLiteral);
    T visit(Literal literal);
    T visit(Reference reference);
    T visit(TypeCheck typeCheck);
    T visit(UnaryExpression unaryExpression);
}
