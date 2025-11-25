package jpp.gametheory.generic;

import java.util.*;

public class GameRound<C extends IChoice> implements IGameRound<C> {

    Map<IPlayer<C>, C> playerChoices;

    public GameRound(Map<IPlayer<C>, C> playerChoices) {
        if (playerChoices.isEmpty())
            throw new IllegalArgumentException();
        this.playerChoices = Objects.requireNonNull(playerChoices);
    }

    @Override
    public Map<IPlayer<C>, C> getPlayerChoices() {
        return playerChoices;
    }

    @Override
    public C getChoice(IPlayer<C> player) {
        Objects.requireNonNull(player);
        if (!playerChoices.containsKey(player))
            throw new IllegalArgumentException();
        return playerChoices.get(player);
    }

    @Override
    public Set<IPlayer<C>> getPlayers() {
        return new HashSet<>(playerChoices.keySet());
    }

    @Override
    public Set<IPlayer<C>> getOtherPlayers(IPlayer<C> player) {
        Objects.requireNonNull(player);
        if (!playerChoices.containsKey(player))
            throw new IllegalArgumentException();
        Set<IPlayer<C>> players = new HashSet<>();
        for (IPlayer<C> player1: playerChoices.keySet()) {
            if (!player1.equals(player))
                players.add(player1);
        }
        return players;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        List<IPlayer<C>> players = new ArrayList<>();
        for (IPlayer<C> player: playerChoices.keySet()) {
            players.add(player);
            Collections.sort(players);
        }
        for (IPlayer<C> player: players) {
            stringBuilder.append(player.getName()).
                    append(" -> ").append(playerChoices.get(player)).append(", ");

        }
        if (stringBuilder.length() > 2)
            stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
