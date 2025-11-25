package jpp.battleship.model;

import java.util.Set;

public interface Ship{

     Set<Coordinate> getCoordinates();

    ShipClass getShipClass();

    Alignment getAlignment();

    Set<Coordinate> getHits();

    boolean isSunk();

    boolean shoot(Coordinate coordinate);

    int hashCode();

    boolean equals(Object other);

    static Ship of(ShipClass shipType, Coordinate reference, Alignment alignment) {
        if (reference == null) throw new NullPointerException();
        return new ShipImpl(shipType, reference, alignment);
    }

    static Ship Carrier(Coordinate reference, Alignment alignment) {
        return of(ShipClass.CARRIER, reference, alignment);
    }

    static Ship Battleship(Coordinate reference, Alignment alignment) {
        return of(ShipClass.BATTLESHIP, reference, alignment);
    }

    static Ship Cruiser(Coordinate reference, Alignment alignment) {
        return of(ShipClass.CRUISER, reference, alignment);
    }

    static Ship Submarine(Coordinate reference, Alignment alignment) {
        return of(ShipClass.SUBMARINE, reference, alignment);
    }

    static Ship Destroyer(Coordinate reference, Alignment alignment) {
        return of(ShipClass.DESTROYER, reference, alignment);
    }
}
