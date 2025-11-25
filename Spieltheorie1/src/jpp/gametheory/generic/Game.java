package jpp.gametheory.generic;

import java.util.*;

public class Game<C extends IChoice> {

    Set<IPlayer<C>> players;
    IReward<C> reward;
    List<IGameRound<C>> gameRounds;

    public Game(Set<IPlayer<C>> players, IReward<C> reward) {
        if (players == null || reward == null)
            throw new NullPointerException();
        if (players.isEmpty())
            throw new IllegalArgumentException();
        this.players = players;
        this.reward = reward;
        gameRounds = new ArrayList<>();

    }

    public Set<IPlayer<C>> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public IGameRound<C> playRound() {
        Map<IPlayer<C>, C> playersMap = new HashMap<>(players.size());
        for (IPlayer<C> player : players) {
            playersMap.put(player, player.getChoice(gameRounds));
        }
        IGameRound<C> gameRound = new GameRound<>(playersMap);
        gameRounds.add(gameRound);
        return gameRound;
    }

    public void playNRounds(int n) {
        if (n < 1)
            throw new IllegalArgumentException();
        while (n-- > 0) {
            playRound();
        }
    }

    public Optional<IGameRound<C>> undoRound() {
        if (gameRounds.isEmpty()) {
            return Optional.empty();
        }

        IGameRound<C> lastRound = gameRounds.remove(gameRounds.size() - 1);
        return Optional.of(lastRound);
    }

    public void undoNRounds(int n) {
        if (n < 1)
            throw new IllegalArgumentException();
        if (n >= gameRounds.size()) {
            gameRounds.clear();
        } else {
            for (int i = 0; i < n; i++) {
                gameRounds.remove(gameRounds.size() - 1);
            }
        }
    }

    public List<IGameRound<C>> getPlayedRounds() {
        return Collections.unmodifiableList(gameRounds);
    }

    public int getPlayerProfit(IPlayer<C> player) {
        if (player == null)
            throw new NullPointerException();
        int result = 0;
        for (IGameRound<C> gameRound : gameRounds) {
            result += reward.getReward(player, gameRound);
        }
        return result;
    }

    public Optional<IPlayer<C>> getBestPlayer() {
        if (gameRounds.isEmpty() || players.isEmpty()) {
            return Optional.empty();
        }
        IPlayer<C> winner = null;
        int max = Integer.MIN_VALUE;
        for (IPlayer<C> player : players) {
            int r = getPlayerProfit(player);
            if (r > max) {
                winner = player;
                max = r;
            }
        }
        assert winner != null;
        return Optional.of(winner);
    }

    @Override
    public String toString() {
        int n = gameRounds.size();
        StringBuilder stringBuilder = new StringBuilder("Spiel nach " + n + " Runden:\n");
        stringBuilder.append("Profit : Spieler\n");

        List<IPlayer<C>> playerList = new ArrayList<>(getPlayers());
        playerList.sort((p1, p2) -> {
            int profit1 = getPlayerProfit(p1);
            int profit2 = getPlayerProfit(p2);
            if (profit1 != profit2) {
                return Integer.compare(profit2, profit1);
            }
            return p1.compareTo(p2);
        });

        for (int i = 0; i < playerList.size(); i++) {
            IPlayer<C> player = playerList.get(i);
            stringBuilder.append(getPlayerProfit(player))
                    .append(" : ")
                    .append(player);
            if (i < playerList.size() - 1) {
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }
}

