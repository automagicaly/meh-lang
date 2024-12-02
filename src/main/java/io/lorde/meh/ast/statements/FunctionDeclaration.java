package io.lorde.meh.ast.statements;

import io.lorde.meh.Token;
import io.lorde.meh.ast.expressions.FunctionParameter;
import io.lorde.meh.ast.types.Type;

import java.util.List;

public final class FunctionDeclaration extends Statement {
    public final Token name;
    public final List<FunctionParameter> parameters;
    public final Body body;
    public final Type returnType;
    public final Context context;

    public FunctionDeclaration(Token name, List<FunctionParameter> parameters, Body body, Type returnType, Context context) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.returnType = returnType;
        this.context = context;
    }

    @Override
    public <T> T accept(StatementVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
