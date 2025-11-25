package jpp.numbergame;

import java.util.Objects;

public class Move {

    Coordinate2D from;
    Coordinate2D to;
    int oldValue;
    int newValue;
    int value;

    public Move(Coordinate2D from, Coordinate2D to, int oldValue, int newValue) {
        if (oldValue < 1 || newValue < 1) {
            throw new IllegalArgumentException();
        }
        this.from = from;
        this.to = to;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Move(Coordinate2D from, Coordinate2D to, int value) {
        this(from, to, value, value );
        if (value < 1) {
            throw new IllegalArgumentException();
        }

    }

    public Coordinate2D getFrom() {
        return from;
    }

    public Coordinate2D getTo() {
        return to;
    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public boolean isMerge(){
        return oldValue != newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move move)) return false;
        return oldValue == move.oldValue && newValue == move.newValue && value == move.value && Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, oldValue, newValue, value);
    }

    public String toString(){
        String s= isMerge()? " (M)":"";

       return  "(" + from.x + "," + from.y + ")" + " = " + oldValue + " -> " + "(" + to.x + "," + to.y +")" + " = " + newValue + s;
    }
}
