package jpp.battleship.model;

public enum Alignment {
    HORIZONTAL,
    VERTICAL;

    public Alignment orthogonal(){
        return switch (this){
            case HORIZONTAL -> VERTICAL;
            case VERTICAL -> HORIZONTAL;
        };
    }
}
