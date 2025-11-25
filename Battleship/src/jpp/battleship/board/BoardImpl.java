package jpp.battleship.board;

import jpp.battleship.model.Coordinate;
import jpp.battleship.model.Ship;

import java.util.*;

public class BoardImpl implements Board{
    int width;
    int height;
    Set<Ship> ships;
    List<Coordinate> shots;

    public BoardImpl(int width, int height, Set<Ship> ships) {
        this.width = width;
        this.height = height;
        this.ships = new HashSet<>(ships);
        this.shots = new ArrayList<>();
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
    public Optional<Ship> getShip(Coordinate coordinate) {
        return ships.stream().filter(ship -> ship.getCoordinates().contains(coordinate)).findFirst();
    }

    @Override
    public Set<Ship> getShips() {
        return Collections.unmodifiableSet(ships);
    }

    @Override
    public List<Coordinate> getShots() {
        return Collections.unmodifiableList(shots);
    }

    @Override
    public boolean isAllSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    @Override
    public boolean canShoot(Coordinate coordinate) {
        if (coordinate == null)
            throw new NullPointerException();
        if (coordinate.getX() < 0 || coordinate.getX() >= width || coordinate.getY() < 0 || coordinate.getY() >= height) {
            return false;
        }
        return !shots.contains(coordinate);
    }

    @Override
    public void shoot(Coordinate coordinate) {
        if (!canShoot(coordinate)) {
            throw new InvalidTargetException(coordinate);
        }
        shots.add(coordinate);
        getShip(coordinate).ifPresent(ship -> ship.shoot(coordinate));
    }
}
