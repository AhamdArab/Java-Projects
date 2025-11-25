package de.uniwue.jpp.compiler.syntax;

import de.uniwue.jpp.compiler.util.NameCache;

import java.util.ArrayList;
import java.util.List;

public class Token {
    TokenKind kind; int position; int data;

    public Token(TokenKind kind, int position, int data) {
        this.kind = kind;
        this.position = position;
        this.data = data;
    }

    public Token(TokenKind kind, int position) {
        this(kind,position,-1);
    }

    public TokenKind getKind() {
        return kind;
    }

    public int getPosition() {
        return position;
    }

    public int getData() {
        return data;
    }

    public int getLength(NameCache names, String source) {
        if (kind == TokenKind.Number) {
            String substring = source.substring(position);
            return substring.split("\\D", 2)[0].length();
        } else if (kind == TokenKind.Identifier) {
            return names.getName(data).length();
        } else if (kind.toString() != null) {
            return kind.toString().length();
        }
        return 1;
    }
}
