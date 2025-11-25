package jpp.battleship.board;

public class IllegalShipPlacementException extends RuntimeException {
    public IllegalShipPlacementException() {
        super();
    }

    public IllegalShipPlacementException(String message) {
        super(message);
    }
}
