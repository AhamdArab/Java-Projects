package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class Cell implements GameFieldElement {
    Optional<GameFieldItem> item;
    GameFieldElement predecessor;
    GameFieldElement successor;
    Coordinate2D pos;

    public Cell(GameFieldItem item, Coordinate2D pos) {
        this.item = Optional.ofNullable(item);
        this.pos = pos;
    }

    @Override
    public Optional<GameFieldItem> getItem() {
        return item;
    }

    @Override
    public GameFieldElementType getType() {
        return GameFieldElementType.CELL;
    }

    @Override
    public Optional<GameFieldElement> getPredecessor() {
        return Optional.ofNullable(predecessor);
    }

    @Override
    public Optional<GameFieldElement> getSuccessor() {
        return Optional.ofNullable(successor);
    }

    @Override
    public Coordinate2D getPos() {
        return pos;
    }

    @Override
    public void setItem(Optional<GameFieldItem> item) {
        this.item = item;
    }

    @Override
    public void setPredecessor(GameFieldElement field) {
        if (field == null)
            throw new IllegalArgumentException("Vorgänger darf nicht null sein.");
        this.predecessor = field;
    }

    @Override
    public void setSuccessor(GameFieldElement field) {
        if (field == null)
            throw new IllegalArgumentException("Nachfolger darf nicht null sein.");
        this.successor = field;
    }

    @Override
    public void update(Collection<Move> moves) {
        if (item.isEmpty()) {
            return;
        }
        if (getItem().isPresent() && getSuccessor().isPresent() && successor.getItem().isEmpty()) {
            if (!item.get().isOnMove()) {
                item.get().startMove(getPos());
            }
            successor.setItem(getItem());
            setItem(Optional.empty());
            successor.update(moves);

        }

        if (getItem().isPresent() && getItem().get().isOnMove()) {
            moves.add(item.get().endMove(getPos()));
        }

        if (getPredecessor().isPresent()) {
            getPredecessor().get().update(moves);
        }
    }



    @Override
    public String toString() {
        return item.map(gameFieldItem -> switch (gameFieldItem.getType()) {
            case BLUE -> "b";
            case GREEN -> "g";
            case RED -> "r";
            case YELLOW -> "y";
            case PURPLE -> "p";
            case BLACK -> "B";
            case ORANGE -> "o";
        }).orElse("n");
    }
}