package io.lorde.meh.ast;

import io.lorde.meh.Meh;
import io.lorde.meh.Token;
import io.lorde.meh.ast.expressions.*;
import io.lorde.meh.ast.statements.FunctionDeclaration;
import io.lorde.meh.ast.types.*;

import java.util.List;
import java.util.Map;

public class TypeChecker implements ExpressionVisitor<Type> {
    private static final List<Class<? extends Type>> integerTypes = List.of(
            IntegerType.class,
            ByteType.class
    );
    private static final List<Class<? extends Type>> numericTypes = List.of(
            IntegerType.class,
            FloatType.class,
            DoubleType.class,
            ByteType.class
    );

    private final Map<String, FunctionDeclaration> functions;

    public TypeChecker(Map<String, FunctionDeclaration> functions) {
        this.functions = functions;
    }

    @Override
    public Type visit(BinaryExpression binaryExpression) {
        Type left = binaryExpression.a.accept(this);
        Type right = binaryExpression.b.accept(this);
        switch (binaryExpression.operator.type()) {
            case EQUAL_TO:
            case NOT_EQUAL_TO:
                return new BooleanType();
            case LESS_OR_EQUAL_TO:
            case LESS_THAN:
            case GREATER_THAN:
            case GREATER_OR_EQUAL_TO:
                assertNumericType(binaryExpression.operator, left, right);
                return new BooleanType();
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
            case PERCENT:
                assertNumericType(binaryExpression.operator, left, right);
                if (left.getClass() != right.getClass()) {
                    throw Meh.error(
                            binaryExpression.operator,
                            "Operands differ in type, left is '%s' and right is '%s'".formatted(
                                    left.friendlyName(),
                                    right.friendlyName()
                            )
                    );
                }
                return left;
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
            case PIPE:
            case AMP:
                assertIntegerType(binaryExpression.operator, left, right);
                return left;

            case AND:
            case OR:
                if (!(left instanceof BooleanType) || !(right instanceof BooleanType)) {
                    throw Meh.error(
                            binaryExpression.operator,
                            "Operator '%s' needs operands to be of type 'bool'"
                    );

                }
                return left;
            default:
                throw Meh.error(
                        binaryExpression.operator,
                        "Binary operator '%s' not recognized while type checking".formatted(binaryExpression.operator.lexeme())
                );
        }
    }

    private static void assertIntegerType(Token operator, Type... types) {
        for (Type t : types) {
            if (!integerTypes.contains(t.getClass())) {
                throw Meh.error(operator, "Operator '%s' needs operands to be of type 'int' or 'byte'");
            }
        }
    }

    private static void assertNumericType(Token operator, Type... types) {
        for (Type t : types) {
            if (!numericTypes.contains(t.getClass())) {
                throw Meh.error(
                        operator,
                        getErrorMessageBothOperandsMustBeNumeric(operator)
                );
            }
        }
    }

    private static String getErrorMessageBothOperandsMustBeNumeric(Token operator) {
        return "Operator '%s' requires both operands to be some kind of numeric types".formatted(operator);
    }

    @Override
    public Type visit(Cast cast) {
        if (!isCastableTo(cast.expression.accept(this), cast.type)) {
            throw Meh.error(cast.keyword, "Expression is not castable to type'%s'".formatted(cast.type.friendlyName()));
        }
        return cast.type;
    }

    private static boolean isCastableTo(Type from, Type to) {
        // TODO check this
        return true;
    }

    @Override
    public Type visit(FunctionCall functionCall) {
        if (functionCall.getLambda().isPresent()) {
            return functionCall.getLambda().get().body.accept(this);
        }
        return functionCall.getName();
    }

    @Override
    public Type visit(Grouping grouping) {
        return null;
    }

    @Override
    public Type visit(IndexedExpression indexedExpression) {
        return null;
    }

    @Override
    public Type visit(Lambda lambda) {
        return null;
    }

    @Override
    public Type visit(ListLiteral listLiteral) {
        return null;
    }

    @Override
    public Type visit(Literal literal) {
        return null;
    }

    @Override
    public Type visit(Reference reference) {
        return null;
    }

    @Override
    public Type visit(TypeCheck typeCheck) {
        return null;
    }

    @Override
    public Type visit(UnaryExpression unaryExpression) {
        return null;
    }
}
