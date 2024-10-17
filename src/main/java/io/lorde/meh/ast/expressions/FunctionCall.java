package io.lorde.meh.ast.expressions;

import io.lorde.meh.Token;

import java.util.List;
import java.util.Optional;

public final class FunctionCall extends Expression {
    private final Token name;
    private final Lambda lambda;
    public List<Expression> arguments;

    public FunctionCall(Token name, Lambda lambda, List<Expression> arguments) {
        this.name = name;
        this.lambda = lambda;
        this.arguments = arguments;
    }

    public Optional<Lambda> getLambda() {
        if (this.lambda ==  null) {
            return Optional.empty();
        }
        return Optional.of(this.lambda);
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
