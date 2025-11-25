package jpp.battleship.board;

import jpp.battleship.model.Ship;

import java.util.Set;

public interface BoardBuilder {
    public int getWidth();

    public int getHeight();

    public boolean canAddShip(Ship ship);

    public BoardBuilder addShip(Ship ship);

    public Set<Ship> getShips();

    public Board build();
}
