package jpp.gametheory.rockPaperScissors.strategies;

import jpp.gametheory.generic.IGameRound;
import jpp.gametheory.generic.IPlayer;
import jpp.gametheory.generic.IStrategy;
import jpp.gametheory.rockPaperScissors.RPSChoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CircleChoice implements IStrategy<RPSChoice> {

    Map<RPSChoice,RPSChoice> choiceMap;

    public CircleChoice() {
        choiceMap = new HashMap<>();
        choiceMap.put(RPSChoice.ROCK,RPSChoice.PAPER);
        choiceMap.put(RPSChoice.PAPER,RPSChoice.SCISSORS);
        choiceMap.put(RPSChoice.SCISSORS,RPSChoice.ROCK);
    }

    @Override
    public String name() {
        return "Circle Choice";
    }

    @Override
    public RPSChoice getChoice(IPlayer<RPSChoice> player, List<IGameRound<RPSChoice>> previousRounds) {
        if (player == null || previousRounds == null)
            throw new NullPointerException();

        if (previousRounds.isEmpty())
            return RPSChoice.ROCK;
        return choiceMap.get(previousRounds.get(previousRounds.size() - 1).getChoice(player));
    }

    @Override
    public String toString() {
        return name();
    }
}
