package jpp.numbergame;

import java.util.Objects;

public class Tile {
    Coordinate2D coord;
    int value;

    public Tile(Coordinate2D coord, int value) {
        if (value<1)
            throw new IllegalArgumentException();
        this.coord = coord;
        this.value = value;
    }

    public Coordinate2D getCoordinate() {
        return coord;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile tile)) return false;
        return value == tile.value && Objects.equals(coord, tile.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, value);
    }
}
