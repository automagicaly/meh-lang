package io.lorde.meh.ast.statements;

import java.util.List;

public final class Body extends Statement{
    public final List<Statement> statements;
    public final Context context;

    public Body(List<Statement> statements, Context context) {
        this.statements = statements;
        this.context = context;
    }

    @Override
    public <T> T accept(StatementVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
