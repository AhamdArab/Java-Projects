package jpp.gametheory.rockPaperScissors;

import jpp.gametheory.generic.IGameRound;
import jpp.gametheory.generic.IPlayer;
import jpp.gametheory.generic.IReward;

import java.util.Set;

public class RPSReward implements IReward<RPSChoice> {

    @Override
    public int getReward(IPlayer<RPSChoice> player, IGameRound<RPSChoice> gameRound) {
        if (player == null || gameRound == null) {
            throw new NullPointerException();
        }

        int reward = 0;
        RPSChoice playerChoice = gameRound.getChoice(player);
        Set<IPlayer<RPSChoice>> playerSet = gameRound.getOtherPlayers(player);

        for (IPlayer<RPSChoice> other : playerSet) {
            RPSChoice otherChoice = gameRound.getChoice(other);

            if (playerChoice == RPSChoice.PAPER && otherChoice == RPSChoice.ROCK) {
                reward += 2;
            } else if (playerChoice == RPSChoice.ROCK && otherChoice == RPSChoice.SCISSORS) {
                reward += 2;
            } else if (playerChoice == RPSChoice.SCISSORS && otherChoice == RPSChoice.PAPER) {
                reward += 2;
            } else if (playerChoice != otherChoice) {

                reward -= 1;
            }

        }

        return reward;
    }
}
