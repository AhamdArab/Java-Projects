package jpp.battleship.board;

import jpp.battleship.model.Coordinate;
import jpp.battleship.model.Ship;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Board {
    public int getWidth();

    public int getHeight();

    public Optional<Ship> getShip(Coordinate coordinate);

    public Set<Ship> getShips();

    public List<Coordinate> getShots();

    public boolean isAllSunk();

    public boolean canShoot(Coordinate coordinate);

    public void shoot(Coordinate coordinate);
}
