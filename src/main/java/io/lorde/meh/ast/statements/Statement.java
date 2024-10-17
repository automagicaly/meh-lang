package io.lorde.meh.ast.statements;

public abstract sealed class Statement permits Body, ExpressionEvaluation, FunctionDeclaration, If, Return, VariableDeclaration {
    abstract public <T> T accept(StatementVisitor<T> astVisitor);
}
