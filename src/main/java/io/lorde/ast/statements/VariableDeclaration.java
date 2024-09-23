package io.lorde.ast.statements;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.expressions.Expression;
import io.lorde.ast.expressions.Identifier;
import io.lorde.ast.types.Type;

import java.util.Optional;

public final class VariableDeclaration extends Statement {
    public final Identifier name;
    private final Type type;
    public final Expression value;

    public VariableDeclaration(Identifier name, Type type, Expression value) {
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
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}