package io.lorde.meh.ast.statements;

import io.lorde.meh.Token;
import io.lorde.meh.ast.expressions.Expression;
import io.lorde.meh.ast.types.Type;

import java.util.Optional;

public final class VariableDeclaration extends Statement {
    public final Token name;
    private final Type type;
    public final Expression value;

    public VariableDeclaration(Token name, Type type, Expression value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Optional<Type> getType() {
        if (this.type == null) {
            return Optional.empty();
        }
        return Optional.of(this.type);
    }

    @Override
    public <T> T accept(StatementVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}