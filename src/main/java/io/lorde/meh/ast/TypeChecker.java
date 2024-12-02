package io.lorde.meh.ast;

import io.lorde.meh.Meh;
import io.lorde.meh.Token;
import io.lorde.meh.ast.expressions.*;
import io.lorde.meh.ast.statements.*;
import io.lorde.meh.ast.types.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeChecker implements ExpressionVisitor<Type>, StatementVisitor<Void> {
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
    private static final List<Class<? extends Type>> nonCastables = List.of(
            StringType.class,
            VoidType.class,
            LambdaType.class,
            FileType.class
    );

    private Context currentContext;
    private FunctionDeclaration currentFunction;

    public TypeChecker(Context globalContext) {
        this.currentContext = globalContext;
        this.currentFunction = null;
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
                assertSameType(binaryExpression.operator, left, right);
                return new BooleanType();

            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
            case PERCENT:
                assertNumericType(binaryExpression.operator, left, right);
                assertSameType(binaryExpression.operator, left, right);
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

    private static boolean functionTypeMatches(List<Type> parametersA, Type returnA, List<Type> parametersB, Type returnB) {
        if (isSameType(returnA, returnB)) {
            return false;
        }

        if (parametersA.size() != parametersB.size()) {
            return false;
        }

        for (int i = 0; i < parametersA.size(); i++) {
            if (!isSameType(parametersA.get(i), parametersB.get(i))) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSameType(Type a, Type b) {
        if (isArrayType(a) || isArrayType(b)) {
            return isArrayType(a) && isArrayType(b) && isSameType(((ArrayType)a).type, ((ArrayType)b).type);
        }

        if (isLambdaType(a) || isLambdaType(b)) {
            LambdaType lambdaA = (LambdaType) a;
            LambdaType lambdaB = (LambdaType) b;

            return functionTypeMatches(lambdaA.inputTypes, lambdaA.outputType, lambdaB.inputTypes, lambdaB.outputType);
        }
        return a.getClass() == b.getClass();
    }

    private static void assertSameType(Token operator, Type left, Type right) {
        if (!isSameType(left, right)) {
            throw Meh.error(
                    operator,
                    "Operands differ in type, left is '%s' and right is '%s'".formatted(
                            left.friendlyName(),
                            right.friendlyName()
                    )
            );
        }
    }

    private static boolean isIntegerType(Type type) {
        return integerTypes.contains(type.getClass());
    }

    private static void assertIntegerType(Token operator, Type... types) {
        for (Type t : types) {
            if (!isIntegerType(t)) {
                throw Meh.error(operator, "Operator '%s' needs operands to be of type 'int' or 'byte'");
            }
        }
    }

    private static boolean isNumericType(Type type) {
        return numericTypes.contains(type.getClass());
    }

    private static void assertNumericType(Token operator, Type... types) {
        for (Type t : types) {
            if (!isNumericType(t)) {
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
            throw Meh.error(cast.keyword, "Expression is not castable to type '%s'".formatted(cast.type.friendlyName()));
        }
        return cast.type;
    }

    private static boolean isAnyType(Type type) {
        return type instanceof AnyType;
    }

    private static boolean isLambdaType(Type type) {
        return type instanceof LambdaType;
    }

    private static boolean isBooleanType(Type type) {
        return type instanceof BooleanType;
    }

    private static boolean isArrayType(Type type) {
        return type instanceof ArrayType;
    }

    private static boolean isNonCastable(Type type) {
        return nonCastables.contains(type.getClass());
    }

    private static boolean isCastableTo(Type from, Type to) {
        if (isAnyType(from) || isAnyType(to)) {
            return true;
        }

        if (isNumericType(from) && isNumericType(to)) {
            return true;
        }

        if (isNonCastable(from) || isNonCastable(to)) {
            return false;
        }

        if ((isBooleanType(from) && isNumericType(to)) || (isNumericType(from) && isBooleanType(to))) {
            return true;
        }

        if (isArrayType(from) || isArrayType(to)) {
            if (!isArrayType(from) || !isArrayType(to)) {
                return false;
            }

            ArrayType arrayFrom = (ArrayType) from;
            ArrayType arrayTo = (ArrayType) to;

            return isCastableTo(arrayFrom.type, arrayTo.type);
        }

        return isSameType(from, to);
    }

    @Override
    public Type visit(FunctionCall functionCall) {

        // TODO: create callable type
        String functionName = functionCall.getName().get().lexeme();

        if (!this.functions.containsKey(functionName)) {
            throw Meh.error(functionCall.getName().get(), "Function '%s' not defined".formatted(functionName));
        }

        FunctionDeclaration fx = this.functions.get(functionName);
        var parametersTypes = fx.parameters.stream().map(FunctionParameter::type).toList();
        var argumentsTypes = functionCall.arguments.stream().map(i -> i.accept(this)).toList();
        if (!functionTypeMatches(parametersTypes, fx.returnType, argumentsTypes, fx.returnType)) {
           throw Meh.error(
                   functionCall.getName().get(),
                   "Function '%s' expected different argument types".formatted(functionName)
           );
        }

        return fx.returnType;
    }

    @Override
    public Type visit(Grouping grouping) {
        return grouping.expression.accept(this);
    }

    @Override
    public Type visit(IndexedExpression indexedExpression) {
        var expressionType = indexedExpression.expr.accept(this);
        var indexType = indexedExpression.index.accept(this);

        if (!isArrayType(expressionType)) {
            throw new Meh.ParserError();
        }

        if (!isNumericType(indexType)) {
            throw new Meh.ParserError();
        }

        return ((ArrayType) expressionType).type;
    }

    @Override
    public Type visit(Lambda lambda) {
        // TODO: infer missing types first
        return lambda.body.accept(this);
    }

    @Override
    public Type visit(ListLiteral listLiteral) {
        var listTypes = listLiteral.list.stream().map(i -> i.accept(this)).toList();
        if (isAllSameType(listTypes)) {
            return new ArrayType(listTypes.get(0));
        }
        return new ArrayType(new AnyType());
    }

    private static boolean isAllSameType(List<Type> listTypes) {
        if (listTypes.isEmpty()) {
            return false;
        }
        Type target = listTypes.getFirst();
        for (var t : listTypes) {
            if (!isSameType(t, target)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Type visit(Literal literal) {
        return literal.type;
    }

    @Override
    public Type visit(Reference reference) {
        Object o = this.currentContext.get(reference.identifier.lexeme());
        switch (o) {
            case null ->
                    throw Meh.error(reference.identifier, "Identifier '%s' not defined.".formatted(reference.identifier.lexeme()));
            case Type t -> {
                if (t instanceof DeferredType) {
                    throw Meh.error(reference.identifier, "Type could not be inferred");
                }
                return t;
            }
            case VariableDeclaration v -> {
                if (v.type instanceof DeferredType) {
                    v.accept(this);
                }
                return v.type;
            }
            default -> {
                throw Meh.error(reference.identifier, "Identifier '%s' type could not be defined.".formatted(reference.identifier.lexeme()));
            }
        }
        // TODO: ref to function declaration
    }

    @Override
    public Type visit(TypeCheck typeCheck) {
        return new BooleanType();
    }

    @Override
    public Type visit(UnaryExpression unaryExpression) {
        switch (unaryExpression.op.type()) {
            case NOT:
                if (!isBooleanType(unaryExpression.expr.accept(this))) {
                    throw Meh.error(
                            unaryExpression.op,
                            "Operation '%s' expects boolean operand".formatted(unaryExpression.op.lexeme())
                    );
                }
                return new BooleanType();

            case PLUS:
            case MINUS:
                var type = unaryExpression.expr.accept(this);

                if (!isNumericType(type)) {
                    throw Meh.error(
                            unaryExpression.op,
                            "Operation '%s' expects numeric operand".formatted(unaryExpression.op.lexeme())
                    );
                }

                return type;

            default:
                throw Meh.error(
                        unaryExpression.op,
                        "Unary operation '%s' not recognized while type checking.".formatted(unaryExpression.op.lexeme())
                );
        }
    }

    @Override
    public Void visit(Body body) {
        Context bck = this.currentContext;
        this.currentContext = body.context;
        for (var i : body.statements) {
            i.accept(this);
        }
        this.currentContext = bck;
        return null;
    }

    @Override
    public Void visit(ExpressionEvaluation expressionEvaluation) {
        expressionEvaluation.expression.accept(this);
        return null;
    }

    @Override
    public Void visit(FunctionDeclaration functionDeclaration) {
        FunctionDeclaration bck = this.currentFunction;
        this.currentFunction = functionDeclaration;
        functionDeclaration.body.accept(this);
        this.currentFunction = bck;
        return null;
    }

    @Override
    public Void visit(If anIf) {
        anIf.ifBody.accept(this);
        if (anIf.getElseBody().isPresent()) {
            anIf.getElseBody().get().accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Return aReturn) {
        if (this.currentFunction == null) {
            throw new RuntimeException("Current function is null while processing return");
        }
        Type exprType = aReturn.expression.accept(this);
        if (!isSameType(this.currentFunction.returnType, exprType)) {
            System.out.printf("Function return type is '%s' not '%s'.%n", this.currentFunction.returnType.toString(), exprType.toString());
            throw new Meh.ParserError();
        }
        return null;
    }

    @Override
    public Void visit(VariableDeclaration variableDeclaration) {
        if (variableDeclaration.type instanceof DeferredType) {
            variableDeclaration.type = variableDeclaration.value.accept(this);
        }
        return null;
    }
}
