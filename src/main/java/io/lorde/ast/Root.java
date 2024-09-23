package io.lorde.ast;

import io.lorde.ast.statements.Statement;

import java.util.ArrayList;
import java.util.List;

public class Root {
    public final List<Statement> statements;

    public Root() {
        statements = new ArrayList<>();
    }

    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
