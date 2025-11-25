package de.uniwue.jpp.testing.enums;

public enum GuestType implements DiscountFactorProvider{
    STUDENT,
    STAFF,
    GUEST;

    public double getDiscountFactor() {
        return switch (this) {
            case STUDENT -> 0.6;
            case STAFF -> 0.8;
            case GUEST -> 1.0;
        };
    }
}
