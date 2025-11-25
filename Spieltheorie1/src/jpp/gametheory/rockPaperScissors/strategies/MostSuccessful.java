package jpp.gametheory.rockPaperScissors.strategies;

import jpp.gametheory.generic.IGameRound;
import jpp.gametheory.generic.IPlayer;
import jpp.gametheory.generic.IReward;
import jpp.gametheory.generic.IStrategy;
import jpp.gametheory.rockPaperScissors.RPSChoice;

import java.util.List;

public class MostSuccessful implements IStrategy<RPSChoice> {

    IStrategy<RPSChoice> alternate;
    IReward<RPSChoice> reward;

    public MostSuccessful(IStrategy<RPSChoice> alternate, IReward<RPSChoice> reward) {
        if (alternate == null || reward == null)
            throw new NullPointerException();
        this.alternate = alternate;
        this.reward = reward;
    }

    @Override
    public String name() {
        return "Most Successful Choice (Alternate: " + alternate.name() + ")";
    }

    @Override
    public RPSChoice getChoice(IPlayer<RPSChoice> player, List<IGameRound<RPSChoice>> previousRounds) {
        if (player == null || previousRounds == null)
            throw new NullPointerException();

        if (previousRounds.isEmpty()) {
            return alternate.getChoice(player, previousRounds);
        }

        int rSuccess = 0;
        int sSuccess = 0;
        int pSuccess = 0;

        for (IGameRound<RPSChoice> round : previousRounds) {
            for (IPlayer<RPSChoice> p : round.getPlayers()) {
                int choiceReward = reward.getReward(p, round);
                RPSChoice choice = round.getChoice(p);

                switch (choice) {
                    case ROCK -> rSuccess += choiceReward;
                    case SCISSORS -> sSuccess += choiceReward;
                    case PAPER -> pSuccess += choiceReward;
                }
            }
        }

        if (rSuccess > sSuccess && rSuccess > pSuccess)
            return RPSChoice.ROCK;
        if (sSuccess > rSuccess && sSuccess > pSuccess)
            return RPSChoice.SCISSORS;
        if (pSuccess > rSuccess && pSuccess > sSuccess)
            return RPSChoice.PAPER;

        return alternate.getChoice(player, previousRounds);
    }

    @Override
    public String toString() {
        return name();
    }
}
