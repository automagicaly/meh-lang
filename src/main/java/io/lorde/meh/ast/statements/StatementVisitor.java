package io.lorde.meh.ast.statements;

public interface StatementVisitor<T> {
    T visit(Body body);
    T visit(ExpressionEvaluation expressionEvaluation);
    T visit(FunctionDeclaration functionDeclaration);
    T visit(If anIf);
    T visit(Return aReturn);
    T visit(VariableDeclaration variableDeclaration);
}
