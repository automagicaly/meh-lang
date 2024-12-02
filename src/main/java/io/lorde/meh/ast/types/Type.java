package io.lorde.meh.ast.types;

public abstract sealed class Type permits AnyType, ArrayType, BooleanType, ByteType, DeferredType, DoubleType, FileType, FloatType, IntegerType, LambdaType, StringType, VoidType {
    abstract public <T> T accept(TypeVisitor<T> visitor);

    public String friendlyName() {
        return this.getClass().getName().replaceAll("Type", "");
    }
}
