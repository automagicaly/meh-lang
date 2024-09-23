package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

import java.util.List;

public final class ListOfLiterals extends Literal {
    public final List<Expression> list;

    public ListOfLiterals(List<Expression> list) {
        this.list = list;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
