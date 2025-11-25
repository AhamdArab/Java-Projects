package jpp.gametheory.rockPaperScissors.strategies;

import jpp.gametheory.generic.IGameRound;
import jpp.gametheory.generic.IPlayer;
import jpp.gametheory.generic.IStrategy;
import jpp.gametheory.rockPaperScissors.RPSChoice;

import java.util.List;

public class SingleChoice implements IStrategy<RPSChoice> {

    RPSChoice choice;
    public SingleChoice(RPSChoice choice) {
        if (choice == null)
            throw new NullPointerException();
        this.choice = choice;
    }

    @Override
    public String name() {
        return "Always " + choice;
    }

    @Override
    public RPSChoice getChoice(IPlayer<RPSChoice> player, List<IGameRound<RPSChoice>> previousRounds) {
        return choice;
    }

    @Override
    public String toString() {
        return name();
    }
}
