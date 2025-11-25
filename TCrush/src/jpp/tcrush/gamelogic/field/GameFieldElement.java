package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Collection;
import java.util.Optional;

public interface GameFieldElement {

    Optional<GameFieldItem> getItem();
    GameFieldElementType getType();
    Optional<GameFieldElement> getPredecessor();
    Optional<GameFieldElement> getSuccessor();
    Coordinate2D getPos();
    void setItem(Optional<GameFieldItem> item);
    void setPredecessor(GameFieldElement field);
    void setSuccessor(GameFieldElement field);
    void update(Collection<Move> moves);

    static GameFieldElement createCell(GameFieldItem item, Coordinate2D pos) {
        if (pos == null) throw new IllegalArgumentException("Position darf nicht null sein.");
        return new Cell(item, pos);
    }

    static GameFieldElement createBlock(Coordinate2D pos) {
        if (pos == null) throw new IllegalArgumentException("Position darf nicht null sein.");
        return new Block(pos);
    }

    static GameFieldElement createFallthrough(Coordinate2D pos) {
        if (pos == null) throw new IllegalArgumentException("Position darf nicht null sein.");
        return new Fallthrough(pos);
    }


}
