package de.uniwue.jpp.compiler.syntax;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.jpp.compiler.util.NameCache;

public class Lexer {
    String text;
    NameCache names;
    int position;

    public Lexer(String text, NameCache names) {
        this(text, names, 0);
    }

    public Lexer(String text, NameCache names, int start) {
        this.text = text;
        this.names = names;
        this.position = start;
    }

    public int getCurrent() {
        return position;
    }

    public void skipWhitespace() {
        while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
            position++;
        }
    }

    public Token lexName() {
        int start = position;
        if (position < text.length() && Character.isLetter(text.charAt(position))) {
            position++;
            while (position < text.length() && (Character.isLetterOrDigit(text.charAt(position)) || text.charAt(position) == '_')) {
                position++;
            }
            String name = text.substring(start, position);

            if (name.equals("let")) {
                return new Token(TokenKind.LetKeyword, start);
            } else if (name.equals("in")) {
                return new Token(TokenKind.InKeyword, start);
            } else {
                int nameIndex = names.cacheName(name);
                return new Token(TokenKind.Identifier, start, nameIndex);
            }
        }
        return new Token(TokenKind.Unknown, start);
    }

    public Token lexNumber() {
        int start = position;
        boolean hasDigits = false;
        while (position < text.length()) {
            char c = text.charAt(position);
            if (c >= '0' && c <= '9') {
                hasDigits = true;
                position++;
            } else if (c == '_' && hasDigits) {
                position++;
            } else {
                break;
            }
        }
        if (!hasDigits) {
            return new Token(TokenKind.Unknown, start);
        }
        String numberString = text.substring(start, position).replace("_", "");
        int value = Integer.parseInt(numberString);
        return new Token(TokenKind.Number, start, value);
    }

    public Token lexToken() {
        skipWhitespace();
        if (position >= text.length()) {
            return new Token(TokenKind.EOF, position);
        }
        char current = text.charAt(position);
        int start = position++;

        switch (current) {
            case '+': return new Token(TokenKind.Plus, start);
            case '-': return new Token(TokenKind.Minus, start);
            case '*': return new Token(TokenKind.Times, start);
            case '/': return new Token(TokenKind.Divide, start);
            case '%': return new Token(TokenKind.Modulo, start);
            case '=': return new Token(TokenKind.Equals, start);
            case '(': return new Token(TokenKind.OpenParen, start);
            case ')': return new Token(TokenKind.CloseParen, start);
            default:
                if (Character.isLetter(current)) {
                    position--;
                    return lexName();
                } else if (Character.isDigit(current)) {
                    position--;
                    return lexNumber();
                }
                return new Token(TokenKind.Unknown, start);
        }
    }

    public List<Token> lexAll() {
        List<Token> tokens = new ArrayList<>();
        while (position < text.length()) {
            skipWhitespace();
            Token token = lexToken();
            tokens.add(token);
            if (token.getKind() == TokenKind.EOF) {
                break;
            }
        }
        tokens.add(new Token(TokenKind.EOF, position));
        return tokens;
    }

    public static List<Token> lex(String text, NameCache names) {
        Lexer lexer = new Lexer(text, names);
        return lexer.lexAll();
    }
}
