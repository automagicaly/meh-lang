package io.lorde.meh.ast.types;

public final class AnyType extends Type {
    @Override
    public <T> T accept(TypeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
