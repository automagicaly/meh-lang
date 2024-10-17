package io.lorde.meh;

import io.lorde.meh.ast.AstVisitor;
import io.lorde.meh.ast.Program;
import io.lorde.meh.ast.expressions.*;
import io.lorde.meh.ast.statements.*;
import io.lorde.meh.ast.types.*;

public class AstPrinter implements AstVisitor<String> {
    private int identation = 0;
    @Override
    public String visit(BinaryExpression binaryExpression) {
        return "(binaryExpression "
                + binaryExpression.operator.lexeme()
                + " "
                + binaryExpression.a.accept(this)
                + " "
                + binaryExpression.b.accept(this)
                + ")";
    }

    @Override
    public String visit(Body body) {
        StringBuilder sb = new StringBuilder();
        sb.append(generateIdentation());
        sb.append("(body \n");
        this.identation++;
        for (Statement s : body.statements) {
            sb.append(generateIdentation());
            sb.append(s.accept(this));
            sb.append("\n");
        }
        this.identation--;
        sb.append(generateIdentation());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Cast cast) {
        return "(cast " + cast.type.accept(this) + " " + cast.expression.accept(this) + ")";
    }
    @Override
    public String visit(ExpressionEvaluation expressionEvaluation) {
        return "(expressionEvaluation " + expressionEvaluation.expression.accept(this) + ")";
    }

    @Override
    public String visit(FunctionCall functionCall) {
        StringBuilder sb = new StringBuilder();
        sb.append("(functionCall ");
        if (functionCall.getName().isPresent()) {
            sb.append(functionCall.getName().get().lexeme());
        } else if (functionCall.getLambda().isPresent()) {
           sb.append(functionCall.getLambda().get().accept(this));
        }
        for (Expression expr : functionCall.arguments) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(FunctionDeclaration functionDeclaration) {
        StringBuilder sb = new StringBuilder();
        sb.append("(functionDeclaration ");
        sb.append(functionDeclaration.name.lexeme());
        sb.append(" ");
        for (FunctionParameter param : functionDeclaration.parameters) {
            sb.append("(");
            sb.append(param.name());
            sb.append(" ");
            sb.append(param.type().accept(this));
            sb.append(") ");
        }
        sb.append("-> ");
        sb.append(functionDeclaration.returnType.accept(this));
        sb.append("\n");
        this.identation++;
        sb.append(functionDeclaration.body.accept(this));
        sb.append("\n");
        this.identation--;
        sb.append(generateIdentation());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Grouping grouping) {
        return "(grouping " + grouping.expression.accept(this) + ")";
    }

    @Override
    public String visit(IndexedExpression indexedExpression) {
        return "(indexedExpression "
                + indexedExpression.expr.accept(this)
                + " "
                + indexedExpression.index.accept(this)
                + ")";
    }

    @Override
    public String visit(If ifStatement) {
        StringBuilder sb = new StringBuilder();
        sb.append("(if ");
        sb.append(ifStatement.condition.accept(this));
        sb.append("\n");
        this.identation++;
        sb.append(ifStatement.ifBody.accept(this));
        sb.append("\n");
        if (ifStatement.getElseBody().isPresent()) {
            sb.append(ifStatement.getElseBody().get().accept(this));
            sb.append("\n");
        }
        this.identation--;
        sb.append(generateIdentation());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Lambda lambda) {
        StringBuilder sb = new StringBuilder();
        sb.append("(lambda ");
        for (FunctionParameter fp : lambda.parameters) {
            sb.append("(");
            sb.append(fp.name());
            sb.append(" ");
            sb.append(fp.type().accept(this));
            sb.append(") ");
        }
        sb.append("-> ");
        sb.append(lambda.body.accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(AnyType anyType) {
        return "(type any)";
    }

    @Override
    public String visit(ArrayType arrayType) {
        return "(type array " + arrayType.type.accept(this) + ")";
    }

    @Override
    public String visit(BooleanType booleanType) {
        return "(type bool)";
    }

    @Override
    public String visit(ByteType byteType) {
        return "(type byte)";
    }

    @Override
    public String visit(DoubleType doubleType) {
        return "(type double)";
    }

    @Override
    public String visit(FileType fileType) {
        return "(type file)";
    }

    @Override
    public String visit(FloatType floatType) {
        return "(type float)";
    }

    @Override
    public String visit(IntegerType integerType) {
        return "(type int)";
    }

    @Override
    public String visit(LambdaType lambdaType) {
        StringBuilder sb = new StringBuilder();
        sb.append("(type lambda ");
        for (Type t : lambdaType.inputTypes) {
            sb.append(t.accept(this));
            sb.append(" ");
        }
        sb.append("-> ");
        sb.append(lambdaType.outputType.accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(StringType stringType) {
        return "(type str)";
    }

    @Override
    public String visit(ListLiteral listLiteral) {
        StringBuilder sb = new StringBuilder();
        sb.append("(list");
        for (Expression e : listLiteral.list) {
            sb.append(" ");
            sb.append(e.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Literal literal) {
        StringBuilder sb = new StringBuilder();
        sb.append("(literal ");
        if (literal.getValue().isPresent()) {
            sb.append("'");
            Token value = literal.getValue().get();
            sb.append(value.literal().toString());
            sb.append("' ");
        }
        sb.append(literal.type.accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(Reference reference) {
        return "(ref " + reference.identifier.lexeme() + ")";
    }

    @Override
    public String visit(Return returnStatement) {
        return "(return " + returnStatement.expression.accept(this) + ")";
    }

    @Override
    public String visit(Program program) {
        StringBuilder sb = new StringBuilder();
        sb.append("(program \n");
        this.identation++;
        for (Statement s: program.statements) {
            sb.append(this.generateIdentation());
            sb.append(s.accept(this));
            sb.append("\n");
        }
        this.identation--;
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(TypeCheck typeCheck) {
        return "(typeCheck " + typeCheck.type.accept(this) + " " + typeCheck.expression.accept(this) + ")";
    }

    @Override
    public String visit(UnaryExpression unaryExpression) {
        return "(unaryExpression "
                + unaryExpression.op
                + " "
                + unaryExpression.expr.accept(this)
                + ")";
    }

    @Override
    public String visit(VariableDeclaration variableDeclaration) {
        StringBuilder sb = new StringBuilder();
        sb.append("(variableDeclaration ");
        sb.append(variableDeclaration.name.lexeme());
        if (variableDeclaration.getType().isPresent()) {
            sb.append(" ");
            sb.append(variableDeclaration.getType().get().accept(this));
        }
        sb.append(" ");
        sb.append(variableDeclaration.value.accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(VoidType voidType) {
        return "(type void)";
    }

    private String generateIdentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.identation; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }
}
