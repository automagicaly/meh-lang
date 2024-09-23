package io.lorde.ast.expressions;

import io.lorde.ast.AstVisitor;

import java.util.Objects;

public final class ComparisonOperator extends BinaryOperator{
    public final Ops op;

    public ComparisonOperator(Ops op) {
        this.op = op;
    }

    @Override
    public <T> T accept(AstVisitor<T> astVisitor) {
        return astVisitor.visit(this);
    }

    public enum Ops {
        LESS("<"),
        LESS_EQUAL("<="),
        EQUAL("=="),
        NOT_EQUAL("!="),
        GREATER_EQUAL(">="),
        GREATER(">");

        public final String symbol;
        Ops(String symbol) {
            this.symbol = symbol;
        }

        public static Ops fromSymbol(String symbol) {
            for(var op : Ops.values()) {
                if (Objects.equals(op.symbol, symbol)) {
                    return op;
                }
            }
            throw new RuntimeException("Invalid symbol");
        }
    }
}
