package io.lorde.ast.statements;

import io.lorde.ast.*;

public abstract sealed class Statement permits ExpressionEvaluation, FunctionDeclaration, If, Return, VariableDeclaration {
    abstract public <T> T accept(AstVisitor<T> astVisitor);
}
