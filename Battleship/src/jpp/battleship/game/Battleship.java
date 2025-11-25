package jpp.battleship.game;

import jpp.battleship.logic.BoardState;
import jpp.battleship.model.Coordinate;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Battleship {
    Player p1;
    Player p2;
    Player currentPlayer;

    public Battleship(Player p1, Player p2) {
        Objects.requireNonNull(p1);
        Objects.requireNonNull(p2);
        if (p1.equals(p2)) {
            throw new IllegalArgumentException();
        }
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = p1;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public Set<Player> getPlayers() {
        return Set.of(p1, p2);
    }

    public Coordinate move() {
        BoardState state = BoardState.create(getOpponent().board());
        Coordinate coordinate = currentPlayer.strategy().get(state);
        getOpponent().board().shoot(coordinate);
        switchCurrentPlayer();
        return coordinate;
    }

    private Player getOpponent() {
        return currentPlayer == p1 ? p2 : p1;
    }

    private void switchCurrentPlayer() {
        currentPlayer = getOpponent();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Optional<Player> getWinner() {
        if (p1.board().isAllSunk()) return Optional.of(p2);
        if (p2.board().isAllSunk()) return Optional.of(p1);
        return Optional.empty();
    }

    public Player run() {
        while (getWinner().isEmpty()) {
            move();
        }
        return getWinner().get();

    }
}
