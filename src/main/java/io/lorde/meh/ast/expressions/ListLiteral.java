package io.lorde.meh.ast.expressions;

import java.util.List;

public final class ListLiteral extends Expression {
    public final List<Expression> list;

    public ListLiteral(List<Expression> list) {
        this.list = list;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
