package de.uniwue.jpp.compiler.syntax;

public enum TokenKind {
    EOF(""),
    Unknown(null),
    Number(null),
    Identifier(null),
    LetKeyword("let"),
    InKeyword("in"),
    Plus("+"),
    Minus("-"),
    Times("*"),
    Divide("/"),
    Modulo("%"),
    Equals("="),
    OpenParen("("),
    CloseParen(")");

    final String string;

    TokenKind(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
