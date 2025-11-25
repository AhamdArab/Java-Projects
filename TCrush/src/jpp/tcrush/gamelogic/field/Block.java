package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Collection;
import java.util.Optional;

public class Block implements GameFieldElement{


    Coordinate2D pos;

    public Block(Coordinate2D pos) {
        this.pos = pos;
    }

    @Override
    public Optional<GameFieldItem> getItem() {
        throw new UnsupportedOperationException("Block enthält kein Item.");
    }

    @Override
    public GameFieldElementType getType() {
        return GameFieldElementType.BLOCK;
    }

    @Override
    public Optional<GameFieldElement> getPredecessor() {
        throw new UnsupportedOperationException("Block hat keinen Vorgänger.");
    }

    @Override
    public Optional<GameFieldElement> getSuccessor() {
        throw new UnsupportedOperationException("Block hat keinen Nachfolger.");
    }

    @Override
    public Coordinate2D getPos() {
        return pos;
    }

    @Override
    public void setItem(Optional<GameFieldItem> item) {
        throw new UnsupportedOperationException("Block kann kein Item setzen.");
    }

    @Override
    public void setPredecessor(GameFieldElement field) {
        throw new UnsupportedOperationException("Block kann keinen Vorgänger setzen.");
    }

    @Override
    public void setSuccessor(GameFieldElement field) {
        throw new UnsupportedOperationException("Block kann keinen Nachfolger setzen.");
    }

    @Override
    public void update(Collection<Move> moves) {
        throw new UnsupportedOperationException("Block unterstützt keine Update-Operationen.");
    }

    @Override
    public String toString() {
        return "#";
    }
}
