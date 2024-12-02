package io.lorde.meh.ast.types;

public interface TypeVisitor<T> {
    T visit(AnyType anyType);
    T visit(ArrayType arrayType);
    T visit(BooleanType booleanType);
    T visit(ByteType byteType);
    T visit(DoubleType doubleType);
    T visit(FileType fileType);
    T visit(FloatType floatType);
    T visit(IntegerType integerType);
    T visit(LambdaType lambdaType);
    T visit(StringType stringType);
    T visit(VoidType voidType);
    T visit(DeferredType deferredType);
}
