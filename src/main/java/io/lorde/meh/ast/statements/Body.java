package io.lorde.meh.ast.statements;

import java.util.List;

public final class Body extends Statement{
    public final List<Statement> statements;

    public Body(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(StatementVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
