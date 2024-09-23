package io.lorde.ast.types;

import io.lorde.ast.AstVisitor;

import java.util.List;

public final class LambdaType extends Type {
    public final List<Type> inputTypes;
    public final Type outputType;

    public LambdaType(List<Type> inputTypes, Type outputTypr) {
        this.inputTypes = inputTypes;
        this.outputType = outputTypr;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }
}
