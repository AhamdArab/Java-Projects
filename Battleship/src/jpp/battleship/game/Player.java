package jpp.battleship.game;

import jpp.battleship.board.Board;
import jpp.battleship.logic.strategy.TargetStrategy;

import java.util.Objects;

public record Player(String name, Board board, TargetStrategy strategy) {
    public Player {
        Objects.requireNonNull(name);
        Objects.requireNonNull(board);
        Objects.requireNonNull(strategy);
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Player player && name.equals(player.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;

    }
}
