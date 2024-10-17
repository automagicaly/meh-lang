package io.lorde.meh.ast;

import io.lorde.meh.ast.statements.Statement;

import java.util.ArrayList;
import java.util.List;

public class Program {
    public final List<Statement> statements;

    public Program() {
        statements = new ArrayList<>();
    }

    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
