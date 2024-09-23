package io.lorde;

import io.lorde.ast.AstVisitor;
import io.lorde.ast.Root;
import io.lorde.ast.expressions.FunctionParameter;
import io.lorde.ast.expressions.*;
import io.lorde.ast.expressions.Void;
import io.lorde.ast.statements.*;
import io.lorde.ast.types.*;

import java.util.HashMap;
import java.util.Map;

public class AstPrinter implements AstVisitor<String> {
    private int tabs = 0;
    private final Map<Integer, String> tabCache = new HashMap<>();

    @Override
    public String visit(Identifier identifier) {
        return identifier.name;
    }

    @Override
    public String visit(IndexedIdentifier indexedIdentifier) {
        return indexedIdentifier.identifier.accept(this) + "[" + indexedIdentifier.index.accept(this) + "]";
    }

    @Override
    public String visit(ListOfLiterals listOfLiterals) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Expression e : listOfLiterals.list) {
            builder.append(e.accept(this));
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String visit(DoubleNumber doubleNumber) {
        return "" + doubleNumber.n;
    }

    @Override
    public String visit(FloatNumber floatNumber) {
        return "" + floatNumber.n;
    }

    @Override
    public String visit(IntegerNumber integerNumber) {
        return "" + integerNumber.n;
    }

    @Override
    public String visit(Bool bool) {
        return "" + bool.state;
    }

    @Override
    public String visit(StringLiteral stringLiteral) {
        return "\"" + stringLiteral.str + "\"";
    }

    @Override
    public String visit(Grouping grouping) {
        return "(" +
                grouping.expression.accept(this) +
                ")";
    }

    @Override
    public String visit(BinaryExpression binaryExpression) {
        return binaryExpression.a.accept(this) + " " + binaryExpression.operator.accept(this) + " " + binaryExpression.b.accept(this);
    }

    @Override
    public String visit(Cast cast) {
        return cast.expression.accept(this) + " as " + cast.type.accept(this);
    }

    @Override
    public String visit(FunctionCall functionCall) {
        StringBuilder builder = new StringBuilder();
        if (functionCall.getLambda().isPresent()) {
            builder.append("( ");
            builder.append(functionCall.getLambda().get().accept(this));
            builder.append(" )");
        } else {
            if (functionCall.getName().isEmpty()) {
                throw new RuntimeException("Either lambda or name must be present on a function call");
            }
            builder.append(functionCall.getName().get().accept(this));
        }
        for (Expression e : functionCall.arguments) {
            builder.append(" ");
            builder.append(e.accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visit(Lambda lambda) {
        StringBuilder builder = new StringBuilder();
        builder.append("\\");
        for (FunctionParameter fp : lambda.parameters) {
            builder.append(fp.name().accept(this));
            if (fp.type() != null) {
                builder.append(": ");
                builder.append(fp.type().accept(this));
            }
            builder.append(" ");
        }
        builder.append("-> ");
        builder.append(lambda.body.accept(this));
        return builder.toString();
    }

    @Override
    public String visit(VariableDeclaration variableDeclaration) {
        StringBuilder builder = new StringBuilder();
        builder.append("let ");
        builder.append(variableDeclaration.name.name);
        if (variableDeclaration.getType().isPresent()) {
            builder.append(": ");
            builder.append(variableDeclaration.getType().get().accept(this));
        }
        builder.append(" = ");
        builder.append(variableDeclaration.value.accept(this));
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public String visit(Return returnStatement) {
        return "return " + returnStatement.expression.accept(this) + "\n";
    }

    @Override
    public String visit(If ifStatement) {
        StringBuilder builder = new StringBuilder();
        builder.append("if ");
        builder.append(ifStatement.condition.accept(this));
        builder.append("\n");
        builder.append(ifStatement.ifBody.accept(this));
        if (ifStatement.getElseBody().isPresent()) {
            builder.append(generateTabs());
            builder.append("else\n");
            builder.append(ifStatement.getElseBody().get().accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visit(Body body) {
        StringBuilder builder = new StringBuilder();
        this.tabs++;
        for (Statement s : body.statements) {
            builder.append(this.generateTabs());
            builder.append(s.accept(this));
            //builder.append("\n");
        }
        this.tabs--;
        return builder.toString();
    }

    @Override
    public String visit(FunctionDeclaration functionDeclaration) {
        StringBuilder builder = new StringBuilder();
        builder.append("\\");
        builder.append(functionDeclaration.name.accept(this));
        if (!functionDeclaration.parameters.isEmpty()) {
            for (FunctionParameter fp: functionDeclaration.parameters) {
                builder.append(" ");
                builder.append(fp.name().accept(this));
                if (fp.type() !=  null) {
                    builder.append(": ");
                    builder.append(fp.type().accept(this));
                }
            }
        }
        builder.append(" -> ");
        builder.append(functionDeclaration.returnType.accept(this));
        builder.append("\n");
        builder.append(functionDeclaration.body.accept(this));
        return builder.toString();
    }

    @Override
    public String visit(BaseType baseType) {
        return baseType.name;
    }

    @Override
    public String visit(ArrayType arrayType) {
        return arrayType.type.accept(this) + "[]";
    }

    @Override
    public String visit(LambdaType lambdaType) {
        StringBuilder builder = new StringBuilder();
        builder.append("(\\");
        for (Type t : lambdaType.inputTypes) {
            builder.append(t.accept(this));
            builder.append(" ");
        }
        builder.append("-> ");
        builder.append(lambdaType.outputType.accept(this));
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visit(AnyType anyType) {
        return "any";
    }

    @Override
    public String visit(TypeCheck typeCheck) {
        return typeCheck.expression.accept(this) + " is " + typeCheck.type.accept(this);
    }

    @Override
    public String visit(BooleanOperator booleanOperator) {
        return booleanOperator.op;
    }

    @Override
    public String visit(NotOperator notOperator) {
        return "not";
    }

    @Override
    public String visit(BitWiseOperator bitWiseOperator) {
        return bitWiseOperator.op;
    }

    @Override
    public String visit(ExpressionEvaluation expressionEvaluation) {
        return expressionEvaluation.expression.accept(this) + "\n";
    }

    @Override
    public String visit(UnaryExpression unaryExpression) {
        return unaryExpression.op.accept(this) + " " + unaryExpression.expr.accept(this);
    }

    @Override
    public String visit(Root root) {
        StringBuilder builder = new StringBuilder();
        for (Statement s : root.statements) {
            builder.append(s.accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visit(NumericOperator numericOperator) {
        return numericOperator.op;
    }

    @Override
    public String visit(ComparisonOperator comparisonOperator) {
        return comparisonOperator.op.symbol;
    }

    @Override
    public String visit(ShiftOperator shiftOperator) {
        return shiftOperator.op;
    }

    @Override
    public String visit(VoidType voidType) {
        return "void";
    }

    @Override
    public String visit(SignOperator signOperator) {
        return signOperator.op;
    }

    @Override
    public String visit(Void aVoid) {
        return "void";
    }

    @Override
    public String visit(Reference reference) {
        return reference.identifier.name;
    }

    @Override
    public String visit(IndexedReference indexedReference) {
        return indexedReference.reference.accept(this) + "[" + indexedReference.index.accept(this) + "]";
    }

    private String generateTabs() {
        if (!this.tabCache.containsKey(this.tabs)) {
            this.tabCache.put(this.tabs, "  ".repeat(this.tabs));
        }
        return this.tabCache.get(this.tabs);
    }
}
