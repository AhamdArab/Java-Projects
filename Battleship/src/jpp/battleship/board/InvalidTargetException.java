package jpp.battleship.board;

import jpp.battleship.model.Coordinate;

public class InvalidTargetException extends RuntimeException {
    public InvalidTargetException() {
        super();
    }

    public InvalidTargetException(Coordinate target) {
        super("Invalid target: " + target);
    }
}
