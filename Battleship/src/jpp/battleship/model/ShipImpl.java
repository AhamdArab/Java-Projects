package jpp.battleship.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ShipImpl implements Ship{
     ShipClass shipClass;
     Alignment alignment;
     Set<Coordinate> coordinates;
     Set<Coordinate> hits;

    public ShipImpl(ShipClass shipClass, Coordinate reference, Alignment alignment) {
        this.shipClass = shipClass;
        this.alignment = alignment;
        this.coordinates = new HashSet<>();
        this.hits = new HashSet<>();

        for (Coordinate c : shipClass.points(alignment)) {
            coordinates.add(reference.add(c));
        }
    }

    @Override
    public Set<Coordinate> getCoordinates() {
        return Collections.unmodifiableSet(coordinates);
    }

    @Override
    public ShipClass getShipClass() {
        return shipClass;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public Set<Coordinate> getHits() {
        return Collections.unmodifiableSet(hits);
    }

    @Override
    public boolean isSunk() {
        return hits.containsAll(coordinates);
    }

    @Override
    public boolean shoot(Coordinate coordinate) {
        if (coordinate == null)
            throw new NullPointerException();
        if (coordinates.contains(coordinate)) {
            hits.add(coordinate);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipImpl ship = (ShipImpl) o;
        return shipClass == ship.shipClass && coordinates.equals(ship.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipClass, coordinates);
    }
}
