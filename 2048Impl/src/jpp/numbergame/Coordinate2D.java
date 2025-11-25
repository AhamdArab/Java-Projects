package jpp.numbergame;

import java.util.Objects;

public class Coordinate2D {

    int x;
    int y;

    public Coordinate2D(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        this.x = x;
        this.y = y;
    }

    public int getX(){

        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate2D that)) return false;
        return x == that.x && y == that.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString(){
        return "("+ x +","+ y +")";
    }

}
