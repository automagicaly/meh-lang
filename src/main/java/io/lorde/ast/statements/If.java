package io.lorde.ast.statements;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.expressions.Body;
import io.lorde.ast.expressions.Expression;

import java.util.Optional;

public final class If extends Statement {
    public final Expression condition;
    public final Body ifBody;
    private final Body elseBody;

    public If(Expression condition, Body ifBody, Body elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    public Optional<Body> getElseBody() {
        if (this.elseBody == null) {
            return Optional.empty();
        }
        return Optional.of(this.elseBody);
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
