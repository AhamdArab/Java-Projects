package de.uniwue.jpp.compiler.error;

import de.uniwue.jpp.compiler.syntax.TokenKind;
import de.uniwue.jpp.compiler.syntax.TokenLocation;
import de.uniwue.jpp.compiler.util.NameCache;

public class ExpectedTokenError implements Error {
    TokenKind expected;
    TokenKind gotten;
    TokenLocation location;

    public ExpectedTokenError(TokenKind expected, TokenKind gotten, TokenLocation location) {
        this.expected = expected;
        this.gotten = gotten;
        this.location = location;
    }

    public TokenKind getExpected() {
        return expected;
    }

    public TokenKind getGotten() {
        return gotten;
    }

    public String getMessage(NameCache names) {
        return "Expected <" + expected + ">, but gotten <" + gotten + ">.";
    }

    public TokenLocation getLocation() {
        return location;
    }
}
