package de.uniwue.jpp.compiler.error;

import de.uniwue.jpp.compiler.syntax.TokenKind;
import de.uniwue.jpp.compiler.syntax.TokenLocation;
import de.uniwue.jpp.compiler.util.NameCache;

public class ExpectedPrimaryExpressionError implements Error {
    TokenKind gotten;
    TokenLocation location;

    public ExpectedPrimaryExpressionError(TokenKind gotten, TokenLocation location) {
        this.gotten = gotten;
        this.location = location;
    }

    public TokenKind getGotten() {
        return gotten;
    }

    public String getMessage(NameCache names) {
        return "Expected primary expression, but gotten <" + gotten + ">.";
    }

    public TokenLocation getLocation() {
        return location;
    }
}
