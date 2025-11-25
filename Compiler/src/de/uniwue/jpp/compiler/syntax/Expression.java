package de.uniwue.jpp.compiler.syntax;

public class Expression {
    ExpressionKind kind;
    int left;
    int right;
    int primaryToken;

    public Expression(ExpressionKind kind, int left, int right, int primaryToken) {
        this.kind = kind;
        this.left = left;
        this.right = right;
        this.primaryToken = primaryToken;
    }

    public ExpressionKind getKind() {
        return kind;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getValue() {
        return left;
    }

    public int getPrimaryToken() {
        return primaryToken;
    }

    public static Expression makeNumber(int value, int primaryToken) {
        return new Expression(ExpressionKind.Number, value, -1, primaryToken);
    }

    public static Expression makeIdentifier(int nameIndex, int primaryToken) {
        return new Expression(ExpressionKind.Identifier, nameIndex, -1, primaryToken);
    }

    public int getFirst(AST ast) {
        if (kind.isBinaryOperation()) {
            return ast.getExpression(left).getFirst(ast);
        }
        return primaryToken;
    }

    public int getLast(AST ast) {
        if (kind == ExpressionKind.Let) {
            return ast.getExpression(right + 1).getLast(ast);
        } else if (kind.isBinaryOperation()) {
            return ast.getExpression(right).getLast(ast);
        }
        return primaryToken;
    }

    public TokenLocation getLocation(AST ast) {
        return new TokenLocation(getFirst(ast), getLast(ast));
    }
}
