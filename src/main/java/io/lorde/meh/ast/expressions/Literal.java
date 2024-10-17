package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;
import io.lorde.meh.ast.types.Type;

import java.util.Optional;

public final class Literal extends Expression {
    private final Token value;
    public final Type type;

    public Literal(Token literal, Type type) {
        this.value = literal;
        this.type = type;
    }

    public Optional<Token> getValue() {
        if (this.value == null) {
            return Optional.empty();
        }
        return Optional.of(this.value);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
