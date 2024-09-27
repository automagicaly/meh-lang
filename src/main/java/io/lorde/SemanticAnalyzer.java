package io.lorde;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.Root;
import io.lorde.ast.expressions.*;
import io.lorde.ast.expressions.Void;
import io.lorde.ast.statements.*;
import io.lorde.ast.types.*;

public class SemanticAnalyzer implements AstVisitor {
    @Override
    public Object visit(AnyType anyType) {
        return null;
    }

    @Override
    public Object visit(ArrayType arrayType) {
        return null;
    }

    @Override
    public Object visit(BaseType baseType) {
        return null;
    }

    @Override
    public Object visit(BinaryExpression binaryExpression) {
        return null;
    }

    @Override
    public Object visit(BitWiseOperator bitWiseOperator) {
        return null;
    }

    @Override
    public Object visit(Body body) {
        return null;
    }

    @Override
    public Object visit(Bool bool) {
        return null;
    }

    @Override
    public Object visit(BooleanOperator booleanOperator) {
        return null;
    }

    @Override
    public Object visit(Cast cast) {
        return null;
    }

    @Override
    public Object visit(ComparisonOperator comparisonOperator) {
        return null;
    }

    @Override
    public Object visit(DoubleNumber doubleNumber) {
        return null;
    }

    @Override
    public Object visit(ExpressionEvaluation expressionEvaluation) {
        return null;
    }

    @Override
    public Object visit(FloatNumber floatNumber) {
        return null;
    }

    @Override
    public Object visit(FunctionCall functionCall) {
        return null;
    }

    @Override
    public Object visit(FunctionDeclaration functionDeclaration) {
        return null;
    }

    @Override
    public Object visit(Grouping grouping) {
        return null;
    }

    @Override
    public Object visit(Identifier identifier) {
        return null;
    }

    @Override
    public Object visit(If ifStatement) {
        return null;
    }

    @Override
    public Object visit(IndexedIdentifier indexedIdentifier) {
        return null;
    }

    @Override
    public Object visit(IndexedReference indexedReference) {
        return null;
    }

    @Override
    public Object visit(IntegerNumber integerNumber) {
        return null;
    }

    @Override
    public Object visit(Lambda lambda) {
        return null;
    }

    @Override
    public Object visit(LambdaType lambdaType) {
        return null;
    }

    @Override
    public Object visit(ListOfLiterals listOfLiterals) {
        return null;
    }

    @Override
    public Object visit(NotOperator notOperator) {
        return null;
    }

    @Override
    public Object visit(NumericOperator numericOperator) {
        return null;
    }

    @Override
    public Object visit(Reference reference) {
        return null;
    }

    @Override
    public Object visit(Return returnStatement) {
        return null;
    }

    @Override
    public Object visit(Root root) {
        return null;
    }

    @Override
    public Object visit(ShiftOperator shiftOperator) {
        return null;
    }

    @Override
    public Object visit(SignOperator signOperator) {
        return null;
    }

    @Override
    public Object visit(StringLiteral stringLiteral) {
        return null;
    }

    @Override
    public Object visit(TypeCheck typeCheck) {
        return null;
    }

    @Override
    public Object visit(UnaryExpression unaryExpression) {
        return null;
    }

    @Override
    public Object visit(VariableDeclaration variableDeclaration) {
        return null;
    }

    @Override
    public Object visit(Void aVoid) {
        return null;
    }

    @Override
    public Object visit(VoidType voidType) {
        return null;
    }
}
