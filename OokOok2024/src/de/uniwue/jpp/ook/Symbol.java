package de.uniwue.jpp.ook;

/**
 * Diese Aufzählung bildet die Symbole bestehend aus einem Token ab.
 */
public enum Symbol {
    Question("Ook?"),
    Exclamation("Ook!"),
    Point("Ook.");

    private final String token;

    Symbol(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
