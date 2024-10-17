package io.lorde.meh.ast;

import io.lorde.meh.ast.expressions.ExpressionVisitor;
import io.lorde.meh.ast.statements.StatementVisitor;
import io.lorde.meh.ast.types.TypeVisitor;

public interface AstVisitor<T> extends StatementVisitor<T>, ExpressionVisitor<T>, TypeVisitor<T> {
    T visit(Program program);
}
