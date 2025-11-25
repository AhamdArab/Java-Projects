package jpp.battleship.model;

import java.util.HashSet;
import java.util.Set;

public enum ShipClass {
    CARRIER(5, "A", "CARRIER"),
    BATTLESHIP(4, "B", "BATTLESHIP"),
    CRUISER(3, "C", "CRUISER"),
    SUBMARINE(3, "S", "SUBMARINE"),
    DESTROYER(2, "D", "DESTROYER");

    final int length;
    final String representation;
    final String name;

    ShipClass(int length, String representation, String name) {
        this.length = length;
        this.representation = representation;
        this.name = name;
    }

    public int length() {
        return length;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return representation;
    }

    public Set<Coordinate> points(Alignment alignment) {
        Set<Coordinate> positions = new HashSet<>();
        int offset = (length - 1) / 2;

        for (int i = 0; i < length; i++) {
            int pos = i - offset;
            if (alignment == Alignment.VERTICAL) {
                positions.add(new Coordinate(0, pos));
            } else {
                positions.add(new Coordinate(pos, 0));
            }
        }
        return positions;
    }
}
