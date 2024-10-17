package io.lorde.meh.ast.types;

public final class ArrayType extends Type {
    public final Type type;

    public ArrayType(Type type) {
        this.type = type;
    }

    @Override
    public <T> T accept(TypeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
