package de.uniwue.jpp.compiler.syntax;

public class TokenLocation {
    int first;
    int last;

    public TokenLocation(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public TokenLocation join(TokenLocation other) {
        return new TokenLocation(this.first, other.last);
    }
}
