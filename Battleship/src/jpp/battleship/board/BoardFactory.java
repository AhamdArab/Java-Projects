package jpp.battleship.board;

import jpp.battleship.model.Alignment;
import jpp.battleship.model.Coordinate;
import jpp.battleship.model.Ship;
import jpp.battleship.model.ShipClass;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class BoardFactory {

    public static BoardBuilder empty(int width, int height) {
        if (width < 2 || height < 2) {
            throw new IllegalArgumentException();
        }
        return new BoardBuilderImpl(width, height);
    }

    public static Board of(int width, int height, Collection<Ship> ships) {
        if (width < 2 || height < 2) {
            throw new IllegalArgumentException();
        }
        if (ships == null) {
            throw new NullPointerException();
        }
        BoardBuilder builder = empty(width, height);
        for (Ship ship : ships) {
            if (ship == null) {
                throw new NullPointerException();
            }
            builder.addShip(ship);
        }
        return builder.build();
    }

    public static Board random() {
        final int width = 10;
        final int height = 10;
        BoardBuilder builder = empty(width, height);
        Random random = new Random();
        Alignment[] alignments = { Alignment.HORIZONTAL, Alignment.VERTICAL };

        for (ShipClass shipClass : ShipClass.values()) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                Coordinate ref = new Coordinate(x, y);
                Alignment alignment = alignments[random.nextInt(alignments.length)];
                Ship ship = switch (shipClass) {
                    case CARRIER -> Ship.Carrier(ref, alignment);
                    case BATTLESHIP -> Ship.Battleship(ref, alignment);
                    case CRUISER -> Ship.Cruiser(ref, alignment);
                    case SUBMARINE -> Ship.Submarine(ref, alignment);
                    case DESTROYER -> Ship.Destroyer(ref, alignment);
                };
                if (builder.canAddShip(ship)) {
                    builder.addShip(ship);
                    placed = true;
                }
            }
        }
        return builder.build();
    }
}
