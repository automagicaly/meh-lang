package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;
import io.lorde.meh.ast.statements.Context;

import java.util.List;
import java.util.Optional;

public final class FunctionCall extends Expression {
    private final Token name;
    public final List<Expression> arguments;

    public FunctionCall(Expression function, List<Expression> arguments) {
        if (function instanceof Reference ref) {
            this.name = ref.identifier;
        } else {
            name = null;
        }
        this.arguments = arguments;
    }

    public Optional<Token> getName() {
        if (this.name == null) {
            return Optional.empty();
        }
        return Optional.of(this.name);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
