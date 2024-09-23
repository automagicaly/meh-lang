package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.statements.Statement;

import java.util.List;

public final class Body extends Expression {
    public final List<Statement> statements;

    public Body(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
