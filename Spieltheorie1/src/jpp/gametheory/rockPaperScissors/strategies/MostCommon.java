package jpp.gametheory.rockPaperScissors.strategies;

import jpp.gametheory.generic.IGameRound;
import jpp.gametheory.generic.IPlayer;
import jpp.gametheory.generic.IStrategy;
import jpp.gametheory.rockPaperScissors.RPSChoice;

import java.util.List;


public class MostCommon implements IStrategy<RPSChoice> {

    IStrategy<RPSChoice> alternate;

    public MostCommon(IStrategy<RPSChoice> alternate) {
        if (alternate == null)
            throw new NullPointerException();
        this.alternate = alternate;
    }

    @Override
    public String name() {
        return "Most Common Choice (Alternate: "+ alternate.name()+")";
    }

    @Override
    public RPSChoice getChoice(IPlayer<RPSChoice> player, List<IGameRound<RPSChoice>> previousRounds) {
        if (player == null || previousRounds == null)
            throw new NullPointerException();

        int rockCount = 0, paperCount = 0, scissorsCount = 0;

        for (IGameRound<RPSChoice> round : previousRounds) {
            for (IPlayer<RPSChoice> p : round.getPlayers()) {
                RPSChoice choice = round.getChoice(p);
                switch (choice) {
                    case ROCK -> rockCount++;
                    case PAPER -> paperCount++;
                    case SCISSORS -> scissorsCount++;
                }
            }
        }

        if (rockCount > paperCount && rockCount > scissorsCount) {
            return RPSChoice.ROCK;
        } else if (paperCount > rockCount && paperCount > scissorsCount) {
            return RPSChoice.PAPER;
        } else if (scissorsCount > rockCount && scissorsCount > paperCount) {
            return RPSChoice.SCISSORS;
        }

        return alternate.getChoice(player, previousRounds);
    }

    @Override
    public String toString() {
        return name();
    }

}
