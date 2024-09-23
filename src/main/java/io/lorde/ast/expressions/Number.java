package io.lorde.ast.expressions;

public abstract sealed class Number extends Literal permits DoubleNumber, FloatNumber, IntegerNumber {
}

