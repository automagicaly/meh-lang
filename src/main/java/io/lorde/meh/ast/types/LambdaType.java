package io.lorde.meh.ast.types;

import java.util.List;

public final class LambdaType extends Type {
    public final List<Type> inputTypes;
    public final Type outputType;

    public LambdaType(List<Type> inputTypes, Type outputTypr) {
        this.inputTypes = inputTypes;
        this.outputType = outputTypr;
    }

    @Override
    public <T> T accept(TypeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
