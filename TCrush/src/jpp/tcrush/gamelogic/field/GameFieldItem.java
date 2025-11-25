package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Objects;

public class GameFieldItem {
    GameFieldItemType type;

    public Coordinate2D startPosition;
    boolean onMove = false;

    public GameFieldItem(GameFieldItemType type) {
        this.type = type;

    }

    public GameFieldItemType getType() {
        return type;
    }

    public void startMove(Coordinate2D startPosition){
        if (Objects.isNull(startPosition))
            throw new IllegalArgumentException();
        if (onMove)
            throw new IllegalStateException();
        this.startPosition =startPosition;
        this.onMove = true;
    }

    public Move endMove(Coordinate2D endPosition){
        if (Objects.isNull(endPosition)){
            throw new IllegalArgumentException();}
        if (!onMove) {
            throw new IllegalStateException();
        }
        Move move = new Move(this.startPosition,endPosition);
        this.startPosition = null;
        this.onMove = false;
        return move;
    }

    public boolean isOnMove(){
        return onMove;
    }


}
