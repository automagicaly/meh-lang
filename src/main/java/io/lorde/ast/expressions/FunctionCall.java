package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

import java.util.List;
import java.util.Optional;

public final class FunctionCall extends Expression {
    private final Identifier name;
    private final Lambda lambda;
    public List<Expression> arguments;

    public FunctionCall(Identifier name, Lambda lambda, List<Expression> arguments) {
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

    public Optional<Identifier> getName() {
        if (this.name == null) {
            return Optional.empty();
        }
        return Optional.of(this.name);
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
