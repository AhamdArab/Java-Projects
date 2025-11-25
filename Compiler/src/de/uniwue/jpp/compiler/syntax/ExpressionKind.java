package de.uniwue.jpp.compiler.syntax;

public enum ExpressionKind {
    Number,
    Identifier,

    Let,

    Add,
    Sub,
    Mul,
    Div,
    Mod;

    public boolean isBinaryOperation() {
        return this == Add || this == Sub || this == Mul || this == Div || this == Mod;
    }

    public static ExpressionKind fromTokenKind(TokenKind tokenKind) {
        return switch (tokenKind) {
            case Plus -> Add;
            case Minus -> Sub;
            case Times -> Mul;
            case Divide -> Div;
            case Modulo -> Mod;
            default ->
                    throw new IllegalArgumentException("TokenKind " + tokenKind + " cannot be converted to ExpressionKind.");
        };
    }

}
