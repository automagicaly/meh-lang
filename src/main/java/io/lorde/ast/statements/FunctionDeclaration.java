package io.lorde.ast.statements;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.expressions.Body;
import io.lorde.ast.expressions.FunctionParameter;
import io.lorde.ast.expressions.Identifier;
import io.lorde.ast.types.Type;

import java.util.List;

public final class FunctionDeclaration extends Statement {
    public final Identifier name;
    public final List<FunctionParameter> parameters;
    public final Body body;
    public final Type returnType;

    public FunctionDeclaration(Identifier name, List<FunctionParameter> parameters, Body body, Type returnType) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.returnType = returnType;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
