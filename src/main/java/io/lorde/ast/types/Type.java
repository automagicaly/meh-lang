package io.lorde.ast.types;

import io.lorde.ast.*;

public abstract sealed class Type permits AnyType, ArrayType, BaseType, LambdaType, VoidType {
    abstract public <T> T accept(AstVisitor<T> astVisitor);
}
