package jpp.battleship.logic;

import jpp.battleship.board.Board;
import jpp.battleship.model.Coordinate;
import jpp.battleship.model.Ship;
import jpp.battleship.model.ShipClass;

import java.util.*;
import java.util.stream.Collectors;

public class BoardStateImpl implements BoardState{
    Board board;

    public BoardStateImpl(Board board) {
        this.board = board;
    }

    @Override
    public int getWidth() {
        return board.getWidth();
    }

    @Override
    public int getHeight() {
        return board.getHeight();
    }

    @Override
    public Set<Ship> getSunkShips() {
        return board.getShips().stream().filter(Ship::isSunk).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<ShipClass> remainingShipClasses() {
        return board.getShips().stream()
                .filter(ship -> !ship.isSunk())
                .map(Ship::getShipClass).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public List<Coordinate> getShots() {
        return Collections.unmodifiableList(board.getShots());
    }

    @Override
    public List<Coordinate> getHits() {
        return board.getShots().stream()
                .filter(coordinate -> board.getShip(coordinate).map(ship -> ship.getHits().contains(coordinate)).orElse(false)).toList();
    }

    @Override
    public List<Coordinate> getDamaged() {
        return board.getShots().stream()
                .filter(coord -> {
                    Optional<Ship> shipOpt = board.getShip(coord);
                    return shipOpt.isPresent() && !shipOpt.get().isSunk();
                }).toList();
    }

    @Override
    public Set<Coordinate> availableTargets() {
        if (board.isAllSunk()) {
            return Collections.emptySet();
        }
        Set<Coordinate> allCoordinates = new HashSet<>();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                Coordinate coordinate = Coordinate.of(x, y);
                if (!board.getShots().contains(coordinate)) {
                    allCoordinates.add(coordinate);
                }
            }
        }
        return Collections.unmodifiableSet(allCoordinates);
    }
}
