package io.lorde.ast;

import io.lorde.ast.expressions.*;
import io.lorde.ast.expressions.Void;
import io.lorde.ast.statements.*;
import io.lorde.ast.types.*;

public interface AstVisitor<T> {
    T visit(Identifier identifier);
    T visit(IndexedIdentifier indexedIdentifier);
    T visit(ListOfLiterals listOfLiterals);
    T visit(DoubleNumber doubleNumber);
    T visit(FloatNumber floatNumber);
    T visit(IntegerNumber integerNumber);
    T visit(Bool bool);
    T visit(StringLiteral stringLiteral);
    T visit(Grouping grouping);
    T visit(BinaryExpression binaryExpression);
    T visit(Cast cast);
    T visit(FunctionCall functionCall);
    T visit(Lambda lambda);
    T visit(VariableDeclaration variableDeclaration);
    T visit(Return returnStatement);
    T visit(If ifStatement);
    T visit(Body body);
    T visit(FunctionDeclaration functionDeclaration);
    T visit(BaseType baseType);
    T visit(ArrayType arrayType);
    T visit(LambdaType lambdaType);
    T visit(AnyType anyType);
    T visit(TypeCheck typeCheck);
    T visit(BooleanOperator booleanOperator);
    T visit(NotOperator notOperator);
    T visit(BitWiseOperator bitWiseOperator);
    T visit(ExpressionEvaluation expressionEvaluation);
    T visit(UnaryExpression unaryExpression);
    T visit(Root root);
    T visit(NumericOperator numericOperator);
    T visit(ComparisonOperator comparisonOperator);
    T visit(ShiftOperator shiftOperator);
    T visit(VoidType voidType);
    T visit(SignOperator signOperator);
    T visit(Void aVoid);
    T visit(Reference reference);
    T visit(IndexedReference indexedReference);
}
