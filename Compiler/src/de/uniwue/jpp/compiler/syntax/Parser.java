package de.uniwue.jpp.compiler.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.uniwue.jpp.compiler.error.Error;
import de.uniwue.jpp.compiler.error.ExpectedPrimaryExpressionError;
import de.uniwue.jpp.compiler.error.ExpectedTokenError;

public class Parser {
    List<Token> tokens;
    int position;
    AST ast;
    List<Error> errors;

    public Parser(List<Token> tokens) {
        this(tokens, 0);
    }

    public Parser(List<Token> tokens, int position) {
        this.tokens = tokens;
        this.position = position;
        this.ast = AST.create();
        this.errors = new ArrayList<>();
    }

    public int getCurrentIndex() {
        return position;
    }

    public AST getAST() {
        return ast;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Token currentToken() {
        if (position < tokens.size()) {
            return tokens.get(position);
        }
        return new Token(TokenKind.EOF, position);
    }

    public Token consumeToken() {
        Token token = currentToken();
        if (position < tokens.size() - 1) {
            position++;
        }
        return token;
    }

    public Token expectToken(TokenKind kind) {
        Token token = currentToken();
        if (token.getKind() == kind) {
            if (position < tokens.size() - 1) {
                return consumeToken();
            } else {
                return token;
            }
        } else {
            errors.add(new ExpectedTokenError(kind, token.getKind(), new TokenLocation(position, position)));
            return new Token(kind, token.getPosition());
        }
    }

    public Optional<Expression> parsePrimaryExpression() {
        Token token = currentToken();
        int tokenIndex = position;
        switch (token.getKind()) {
            case Number -> {
                consumeToken();
                return Optional.of(Expression.makeNumber(token.getData(), tokenIndex));
            }
            case Identifier -> {
                consumeToken();
                return Optional.of(Expression.makeIdentifier(token.getData(), tokenIndex));
            }
            case OpenParen -> {
                consumeToken();
                Optional<Expression> innerExpression = parseExpression();
                expectToken(TokenKind.CloseParen);
                return innerExpression;
            }
            default -> {
                errors.add(new ExpectedPrimaryExpressionError(token.getKind(), new TokenLocation(position, position)));
                return Optional.empty();
            }
        }
    }

    public Optional<Expression> parseBinaryMultiplicative() {
        return parseBinaryExpression(this::parsePrimaryExpression, TokenKind.Times, TokenKind.Divide, TokenKind.Modulo);
    }

    public Optional<Expression> parseBinaryAdditive() {
        return parseBinaryExpression(this::parseBinaryMultiplicative, TokenKind.Plus, TokenKind.Minus);
    }

    private Optional<Expression> parseBinaryExpression(
            java.util.function.Supplier<Optional<Expression>> subExpressionParser,
            TokenKind... operators) {
        Optional<Expression> leftOpt = subExpressionParser.get();
        if (leftOpt.isEmpty()) {
            return Optional.empty();
        }
        Expression left = leftOpt.get();

        while (true) {
            Token token = currentToken();
            boolean isOperator = false;
            for (TokenKind operator : operators) {
                if (token.getKind() == operator) {
                    isOperator = true;
                    break;
                }
            }
            if (!isOperator) {
                break;
            }
            consumeToken();
            Optional<Expression> rightOpt = subExpressionParser.get();
            if (rightOpt.isEmpty()) {
                return Optional.of(left);
            }
            Expression right = rightOpt.get();
            int leftIndex = ast.addExpression(left);
            int rightIndex = ast.addExpression(right);
            left = new Expression(ExpressionKind.fromTokenKind(token.getKind()), leftIndex, rightIndex, token.getPosition());
        }
        return Optional.of(left);
    }

    public Optional<Expression> parseLetExpression() {
        Token letToken = expectToken(TokenKind.LetKeyword);
        Token identifierToken = expectToken(TokenKind.Identifier);
        expectToken(TokenKind.Equals);
        Optional<Expression> assignmentExpressionOpt = parseExpression();
        expectToken(TokenKind.InKeyword);
        Optional<Expression> innerExpressionOpt = parseExpression();

        if (assignmentExpressionOpt.isEmpty() || innerExpressionOpt.isEmpty()) {
            return Optional.empty();
        }

        int assignmentIndex = ast.addExpression(assignmentExpressionOpt.get());
        ast.addExpression(innerExpressionOpt.get());
        return Optional.of(new Expression(ExpressionKind.Let, identifierToken.getData(), assignmentIndex, letToken.getPosition()));
    }

    public Optional<Expression> parseExpression() {
        if (currentToken().getKind() == TokenKind.LetKeyword) {
            return parseLetExpression();
        } else {
            return parseBinaryAdditive();
        }
    }

    public Optional<Integer> parseAll() {
        Optional<Expression> expressionOpt = parseExpression();
        expectToken(TokenKind.EOF);
        if (expressionOpt.isPresent()) {
            int expressionIndex = ast.addExpression(expressionOpt.get());
            return Optional.of(expressionIndex);
        } else {
            return Optional.empty();
        }
    }
}
