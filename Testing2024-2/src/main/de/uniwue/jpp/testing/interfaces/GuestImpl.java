package de.uniwue.jpp.testing.interfaces;

import de.uniwue.jpp.testing.enums.GuestType;

import java.util.Objects;

public class GuestImpl implements Guest {
    String name;
    GuestType type;

    public GuestImpl(String name, GuestType type) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null!");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty!");
        }
        if (type == null) {
            throw new IllegalArgumentException("GuestType must not be null!");
        }
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GuestType getGuestType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestImpl guest)) return false;
        return Objects.equals(name, guest.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
