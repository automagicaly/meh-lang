package io.lorde.meh;

import io.lorde.meh.ast.Program;
import io.lorde.meh.ast.expressions.*;
import io.lorde.meh.ast.statements.*;
import io.lorde.meh.ast.types.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.lorde.meh.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int current;
    private final Stack<Context> contextStack;
    private final Set<String> globalVariables;
    private boolean isEmptyLine;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
        this.globalVariables = new HashSet<>();
        this.contextStack = new Stack<>();
        contextStack.push(new Context(globalVariables, 0));
        this.isEmptyLine = false;
    }

    public Program parse() {
        Program program = new Program();
        while (!isAtEnd()) {
            Statement statement = statement();
            if (statement instanceof VariableDeclaration) {
                globalVariables.add(((VariableDeclaration) statement).name.lexeme());
            }
            program.statements.add(statement);
            skipSpacesAndNewLines();
        }
        return program;
    }

    private Statement statement() {
        if (match(LET)) {
            return variableDeclationStatement();
        }

        if (match(IF)) {
            return ifStatement();
        }

        if (match(RETURN)) {
            return returnStatement();
        }

        if (match(BACK_SLASH)) {
            return functionDeclarationStatement();
        }

        return expressionEvaluationStatement();
    }

    private Statement expressionEvaluationStatement() {
        return new ExpressionEvaluation(expression());
    }

    private void openContext() {
        contextStack.push(new Context(new HashSet<>(contextStack.peek().variables), contextStack.peek().indentation));
    }

    private void closeContext() {
        contextStack.pop();
    }

    private void addVariable(String name) {
        contextStack.peek().variables().add(name);
    }

    private void setIndentation(int i) {
        contextStack.push(contextStack.pop().setIndentation(i));
    }

    private boolean hasVariable(String name) {
        return contextStack.peek().variables.contains(name);
    }

    private int contextIndentation() {
        return contextStack.peek().indentation;
    }

    private Statement functionDeclarationStatement() {
        openContext();
        Token name = identifier();
        skipSpaces();
        List<FunctionParameter> parameters = new ArrayList<>();
        while(!check(ARROW)) {
            String identifier = identifier().lexeme();
            skipSpaces();
            consume(COLON, "Missing ':'");
            skipSpaces();
            Type type = type();
            if (!(type instanceof LambdaType)) {
                addVariable(identifier);
            }
            skipSpaces();
            parameters.add(new FunctionParameter(identifier, type));
        }
        consume(ARROW, "Missing '->'");
        skipSpaces();
        Type returnType = type();
        skipSpaces();
        consume(NEW_LINE, "Missing '\\n'");
        Body body = body();
        closeContext();
        return new FunctionDeclaration(name, parameters, body, returnType);
    }

    private Statement returnStatement() {
        skipSpaces();
        return new Return(expression());
    }

    private Statement ifStatement() {
        openContext();
        int bckIndentation = contextIndentation();
        skipSpaces();
        Expression condition = expression();
        skipSpaces();
        consume(NEW_LINE, "Missing new line character");
        Body ifBody = body();
        setIndentation(bckIndentation);
        Body elseBody = null;
        if (indentation() == contextIndentation() && lookAheadForElse()) {
            skipSpaces();
            consume(ELSE, "IDK 3");
            consume(NEW_LINE, "Missing new line character");
            elseBody = body();
        }
        closeContext();
        return new If(condition, ifBody, elseBody);
    }

    private boolean lookAheadForElse() {
        int bck = this.current;
        while(!match(NEW_LINE) && !isAtEnd()) {
            if (check(TokenType.ELSE)) {
                this.current = bck;
                return true;
            }
            advance();
        }
        this.current = bck;
        return false;
    }

    private Statement variableDeclationStatement() {
        skipSpaces();
        Token identifier = identifier();
        skipSpaces();
        Type type = optionalType();
        skipSpaces();
        consume(EQUALS, "Variable declaration must contains a '='");
        skipSpaces();
        Expression expr = expression();
        skipSpaces();
        if (expr instanceof Lambda) {
            List<Expression> args = new ArrayList<>();
            for (int i = 0; i < ((Lambda) expr).parameters.size(); i++) {
                args.add(expression());
                skipSpaces();
            }
            expr = new FunctionCall(null, (Lambda) expr, args);
        }
        consume(NEW_LINE, "Missing new line character");
        return new VariableDeclaration(identifier, type, expr);
    }


    private Type optionalType() {
        if (!match(COLON)) {
           return null;
        }
        skipSpaces();
        return type();
    }

    private Type type() {
        Type baseType = baseType();

        if (match(OPEN_SQUARE_BRACKET) && !(baseType instanceof VoidType)) {
            consume(CLOSE_SQUARE_BRACKET, "Missing ']'");
            baseType = new ArrayType(baseType);
        }
        return baseType;
    }

    private Type baseType() {
        Map<TokenType, Supplier<Type>> easyTypes =  new HashMap<>() ;
        easyTypes.put(ANY, AnyType::new);
        easyTypes.put(BOOL, BooleanType::new);
        easyTypes.put(BYTE, ByteType::new);
        easyTypes.put(DOUBLE, DoubleType::new);
        easyTypes.put(FILE, FileType::new);
        easyTypes.put(FLOAT, FloatType::new);
        easyTypes.put(INTEGER, IntegerType::new);
        easyTypes.put(STRING, StringType::new);
        easyTypes.put(VOID, VoidType::new);

        switch (peek().type()) {
            case BOOL:
            case INTEGER:
            case DOUBLE:
            case FLOAT:
            case STRING:
            case BYTE:
            case FILE:
            case ANY:
            case VOID:
                TokenType type = advance().type();
                return easyTypes.get(type).get();

            // matching a lambda type
            case OPEN_PARENTHESIS:
                advance();
                skipSpaces();
                consume(BACK_SLASH, "Lambda type declaration missing '\\'");
                List<Type> inputTypes = new ArrayList<>();
                skipSpaces();
                while(!check(ARROW)) {
                    inputTypes.add(type());
                    skipSpaces();
                }
                skipSpaces();
                consume(ARROW, "Lambda type declaration missing '->'");
                skipSpaces();
                Type outputType =  type();
                skipSpaces();
                consume(CLOSE_PARENTHESIS, "Lambda type declaration missing ')'");
                return new LambdaType(inputTypes, outputType);

            default:
                throw Meh.error(peek(), "Invalid type: "+peek().lexeme());
        }

    };

    private Token consume(TokenType type, String errorMessage) {
        if (check(type)) {
            return advance();
        }
        throw Meh.error(peek(), errorMessage);
    }

    private void skipSpaces() {
        while(check(SPACE)) {
            advance();
        }
    }

    private Token identifier() {
        return consume(IDENTIFIER, "Missing identifier");
    }

    private Body body() {
        int targetIndentation = indentation();
        if (targetIndentation == 0) {
            throw Meh.error(peek(), "Missing indentation");
        }
        setIndentation(targetIndentation);
        List<Statement> statements = new ArrayList<>();
        while(indentation() == targetIndentation || isEmptyLine) {
            skipSpaces();
            if (!isEmptyLine) {
                statements.add(statement());
            }
            match(NEW_LINE);
        }
        return new Body(statements);
    }


    private Expression expression() {
        if (check(IDENTIFIER) && !hasVariable(peek().lexeme())) {
            Token identifier = identifier();
            skipSpaces();
            List<Expression> args = new ArrayList<>();
            while (!check(NEW_LINE) && !check(EOF) && !check(CLOSE_PARENTHESIS) && !check(COMMA) && !check(CLOSE_SQUARE_BRACKET)){
                args.add(expression());
                skipSpaces();
            }
            return new FunctionCall(identifier, null, args);
        }

        /*
         * or
         * and
         * not
         * |
         * &
         * << >>
         * == !=
         * < <= >= >
         * / * %
         * + -
         * unary + -
         * as is
         * numbers, strings, true, false, ( expr ), void, [ expr, expr, ...]
         */
        return orExpression();
    }

    private Expression orExpression() {
        Expression expr = andExpression();
        skipSpaces();
        while(match(OR)) {
            Token operator = previous();
            skipSpaces();
            Expression right = andExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression andExpression() {
        Expression expr = notExpression();
        skipSpaces();
        while(match(AND)) {
            Token operator = previous();
            skipSpaces();
            Expression right = notExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression notExpression() {
        if (match(NOT)) {
            Token operator = previous();
            skipSpaces();
            return new UnaryExpression(operator, notExpression());
        }
        return bitWiseOrExpression();
    }

    private Expression bitWiseOrExpression() {
        Expression expr = bitWiseAndExpression();
        skipSpaces();
        while(match(PIPE)) {
            Token operator = previous();
            skipSpaces();
            Expression right = bitWiseAndExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression bitWiseAndExpression() {
        Expression expr = shiftExpression();
        skipSpaces();
        while(match(AMP)) {
            Token operator = previous();
            skipSpaces();
            Expression right = shiftExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression shiftExpression() {
        Expression expr = equalityExpression();
        skipSpaces();
        while(match(SHIFT_LEFT, SHIFT_RIGHT)) {
            Token operator = previous();
            skipSpaces();
            Expression right = equalityExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression equalityExpression() {
        Expression expr = comparisonExpression();
        skipSpaces();
        while(match(EQUAL_TO, NOT_EQUAL_TO)) {
            Token operator = previous();
            skipSpaces();
            Expression right = comparisonExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression comparisonExpression() {
        Expression expr = multiplyDivideExpression();
        skipSpaces();
        while(match(LESS_THAN, LESS_OR_EQUAL_TO, GREATER_OR_EQUAL_TO, GREATER_THAN)) {
            Token operator = previous();
            skipSpaces();
            Expression right = multiplyDivideExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression multiplyDivideExpression() {
        Expression expr = addSubtractExpression();
        skipSpaces();
        while(match(SLASH, PERCENT, STAR)) {
            Token operator = previous();
            skipSpaces();
            Expression right = addSubtractExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression addSubtractExpression() {
        Expression expr = unaryExpression();
        skipSpaces();
        while(match(MINUS, PLUS)) {
            Token operator = previous();
            skipSpaces();
            Expression right = unaryExpression();
            skipSpaces();
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression unaryExpression() {
        if (match(MINUS, PLUS)) {
            return new UnaryExpression(previous(), unaryExpression());
        }
        return typeExpression();
    }

    private Expression typeExpression() {
        Expression expr = literalAndGroupingExpression();
        skipSpaces();
        if (match(AS)) {
            Token keyword = previous();
            skipSpaces();
            return new Cast(keyword, expr, type());
        }
        if (match(IS)) {
            skipSpaces();
            return new TypeCheck(expr, type());
        }
        return expr;
    }

    private Expression literalAndGroupingExpression() {
        if (match(NUMBER_INTEGER)) {
            return new Literal(previous(), new IntegerType());
        }
        if (match(NUMBER_FLOAT)) {
            return new Literal(previous(), new DoubleType());
        }
        if (match(STRING_LITERAL)) {
            return new Literal(previous(), new StringType());
        }
        if (match(TRUE, FALSE)) {
            return new Literal(previous(), new BooleanType());
        }
        if (match(VOID)) {
            return new Literal(null, new VoidType());
        }
        if (check(IDENTIFIER)) {
            Reference ref = new Reference(identifier());
            if (match(OPEN_SQUARE_BRACKET)) {
                Expression expr = expression();
                skipSpaces();
                consume(CLOSE_SQUARE_BRACKET, "Missing ']'");
                return new IndexedExpression(ref, expr);
            }
            return ref;
        }
        if (match(OPEN_PARENTHESIS)) {
            if (match(BACK_SLASH)) {
                openContext();
                skipSpaces();
                List<FunctionParameter> parameters = new ArrayList<>();
                while(!check(ARROW)) {
                    Token identifier = identifier();
                    skipSpaces();
                    Type type = optionalType();
                    skipSpaces();
                    if (!(type instanceof LambdaType)) {
                        addVariable(identifier.lexeme());
                    }
                    parameters.add(new FunctionParameter(identifier.lexeme(), type));
                }
                consume(ARROW, "Missing '->'");
                skipSpaces();
                Expression expr = expression();
                skipSpaces();
                consume(CLOSE_PARENTHESIS, "Missing ')'");
                closeContext();
                return new Lambda(parameters, expr);
            }
            skipSpaces();
            Expression expr = expression();
            skipSpaces();
            if (expr instanceof Lambda l) {
                List<Expression> args = new ArrayList<>();
                for (int i = 0; i < l.parameters.size(); i++) {
                    args.add(expression());
                    skipSpaces();
                }
                expr = new FunctionCall(null, l, args);
            }
            consume(CLOSE_PARENTHESIS, "Missing ')'");
            return new Grouping(expr);
        }
        if (match(OPEN_SQUARE_BRACKET)) {
            skipSpacesAndNewLines();
            List<Expression> expressions = new ArrayList<>();
            while(!check(CLOSE_SQUARE_BRACKET)) {
                expressions.add(expression());
                skipSpacesAndNewLines();
                if (!check(CLOSE_SQUARE_BRACKET)) {
                    consume(COMMA, "Missing ',' on a list");
                    skipSpacesAndNewLines();
                }
            }
            consume(CLOSE_SQUARE_BRACKET, "Missing ']' on a list");
            return new ListLiteral(expressions);
        }
        throw Meh.error(peek(), "Could not match literal");
    }

    private void skipSpacesAndNewLines() {
        while(check(SPACE) || check(NEW_LINE)){
            advance();
        }
    }

    private int indentation() {
        this.isEmptyLine = false;
        int bck = this.current;
        int spacesCount = 0;
        while(check(SPACE)) {
            spacesCount++;
            advance();
        }
        if (check(NEW_LINE)) {
            this.isEmptyLine = true;
        }
        this.current = bck;
        return spacesCount;
    }


    private boolean match(TokenType... types) {
        for(var t : types) {
            if (check(t)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().type() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private Token peek() {
        return this.tokens.get(this.current);
    }

    private Token previous() {
        return this.tokens.get(this.current - 1);
    }

    private boolean isAtEnd() {
        return  peek().type() == EOF;
    }

    private record Context(Set<String> variables, int indentation) {
        public Context setIndentation(int i) {
            return deepCopy(i);
        }

        private Context deepCopy(int i) {
            return new Context(variables, i);
        }

    }
}
