package jpp.tcrush.gamelogic.utils;

public class Move {

    Coordinate2D from;
    Coordinate2D to;

    public Move(Coordinate2D from, Coordinate2D to) {
        if (from == null || to == null)
            throw new IllegalArgumentException();
        this.from = from;
        this.to = to;
    }

    public Coordinate2D getFrom() {
        return from;
    }

    public Coordinate2D getTo() {
        return to;
    }
}
