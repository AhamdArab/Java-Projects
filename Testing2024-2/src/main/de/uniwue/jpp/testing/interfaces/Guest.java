package de.uniwue.jpp.testing.interfaces;

import de.uniwue.jpp.testing.enums.GuestType;

public interface Guest {
    public String getName();

    public GuestType getGuestType();

    public static Guest createGuest(String name, GuestType type){

        return new GuestImpl(name,type);
    }

    default double getDiscountFactor() {
        return getGuestType().getDiscountFactor();
    }
}
