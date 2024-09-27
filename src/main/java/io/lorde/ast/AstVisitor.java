package io.lorde.ast;

import io.lorde.ast.expressions.*;
import io.lorde.ast.expressions.Void;
import io.lorde.ast.statements.*;
import io.lorde.ast.types.*;

public interface AstVisitor<T> {
    T visit(AnyType anyType);
    T visit(ArrayType arrayType);
    T visit(BaseType baseType);
    T visit(BinaryExpression binaryExpression);
    T visit(BitWiseOperator bitWiseOperator);
    T visit(Body body);
    T visit(Bool bool);
    T visit(BooleanOperator booleanOperator);
    T visit(Cast cast);
    T visit(ComparisonOperator comparisonOperator);
    T visit(DoubleNumber doubleNumber);
    T visit(ExpressionEvaluation expressionEvaluation);
    T visit(FloatNumber floatNumber);
    T visit(FunctionCall functionCall);
    T visit(FunctionDeclaration functionDeclaration);
    T visit(Grouping grouping);
    T visit(Identifier identifier);
    T visit(If ifStatement);
    T visit(IndexedIdentifier indexedIdentifier);
    T visit(IndexedReference indexedReference);
    T visit(IntegerNumber integerNumber);
    T visit(Lambda lambda);
    T visit(LambdaType lambdaType);
    T visit(ListOfLiterals listOfLiterals);
    T visit(NotOperator notOperator);
    T visit(NumericOperator numericOperator);
    T visit(Reference reference);
    T visit(Return returnStatement);
    T visit(Root root);
    T visit(ShiftOperator shiftOperator);
    T visit(SignOperator signOperator);
    T visit(StringLiteral stringLiteral);
    T visit(TypeCheck typeCheck);
    T visit(UnaryExpression unaryExpression);
    T visit(VariableDeclaration variableDeclaration);
    T visit(Void aVoid);
    T visit(VoidType voidType);
}
