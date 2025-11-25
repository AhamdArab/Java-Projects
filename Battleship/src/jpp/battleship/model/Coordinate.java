package jpp.battleship.model;

import java.util.Objects;
import java.util.Optional;

public class Coordinate {
    int x; int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate add(Coordinate other) {
        if (other == null)
            throw new NullPointerException();
        return new Coordinate(other.getX() + this.x,other.getY() + this.y);
    }

    public Optional<Alignment> computeAlignment(Coordinate other) {
        if (other == null)
            throw new NullPointerException();

        if (this.equals(other)) {
            return Optional.empty();
        }
        if (this.getX() == other.getX()) {
            return Optional.of(Alignment.VERTICAL);
        }
        if (this.getY() == other.getY()) {
            return Optional.of(Alignment.HORIZONTAL);
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }

    public static Coordinate of(int x, int y) {
        return new Coordinate(x, y);
    }

    public static Optional<Coordinate> fromBattleshipString(String coordinate) {
        if (coordinate == null) {
            throw new NullPointerException();
        }
        char let = coordinate.charAt(0);
        if (let < 'A' || let > 'Z') {
            return Optional.empty();
        }

        int y = let - 'A';
        int x = 0;

        for (int i = 1; i < coordinate.length(); i++) {
            char c = coordinate.charAt(i);
            if (c < '0' || c > '9') {
                return Optional.empty();
            }
            x = x * 10 + (c - '0');
        }

        x -= 1;
        if (x < 0) {
            return Optional.empty();
        }

        return Optional.of(new Coordinate(x, y));
    }
}
