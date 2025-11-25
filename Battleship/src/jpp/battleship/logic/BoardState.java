package jpp.battleship.logic;

import jpp.battleship.board.Board;
import jpp.battleship.model.Coordinate;
import jpp.battleship.model.Ship;
import jpp.battleship.model.ShipClass;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BoardState {
    public int getWidth();

    public int getHeight();

    public Set<Ship> getSunkShips();

    public Set<ShipClass> remainingShipClasses();

    public List<Coordinate> getShots();

    public List<Coordinate> getHits();

    public List<Coordinate> getDamaged();

    public Set<Coordinate> availableTargets();

    public static BoardState create(Board board){
        Objects.requireNonNull(board);
        return new BoardStateImpl(board);
    }
}
