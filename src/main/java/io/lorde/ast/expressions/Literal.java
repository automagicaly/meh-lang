package io.lorde.ast.expressions;

public abstract sealed class Literal extends Expression permits Bool, ListOfLiterals, Number, StringLiteral, Void {
}
