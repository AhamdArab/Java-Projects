package jpp.battleship.board;

import jpp.battleship.model.Coordinate;
import jpp.battleship.model.Ship;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BoardBuilderImpl implements BoardBuilder{
    int width;
    int height;
    Set<Ship> ships;

    public BoardBuilderImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.ships = new HashSet<>();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean canAddShip(Ship ship) {
        if (ship == null)
            throw new NullPointerException();
        if (ships.stream().anyMatch(s -> s.getShipClass() == ship.getShipClass()))
            return false;
        for (Coordinate c : ship.getCoordinates()) {
            if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height)
                return false;
            if (ships.stream().anyMatch(s -> s.getCoordinates().contains(c)))
                return false;
        }
        return true;
    }

    @Override
    public BoardBuilder addShip(Ship ship) {
        if (ship == null)
            throw new NullPointerException();
        if (ships.stream().anyMatch(s -> s.getShipClass() == ship.getShipClass())) {
            throw new IllegalShipPlacementException("Ship already exists");
        }
        for (Coordinate c : ship.getCoordinates()) {
            if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height) {
                throw new IllegalShipPlacementException("Illegal ship position");
            }
        }
        for (Coordinate c : ship.getCoordinates()) {
            if (ships.stream().anyMatch(s -> s.getCoordinates().contains(c))) {
                throw new IllegalShipPlacementException("Overlaps with existing ship");
            }
        }
        ships.add(ship);
        return this;
    }

    @Override
    public Set<Ship> getShips() {
        return Collections.unmodifiableSet(ships);
    }

    @Override
    public Board build() {
        return new BoardImpl(width, height, ships);
    }
}
