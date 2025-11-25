package jpp.battleship.game;

import jpp.battleship.logic.strategy.TargetStrategy;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public TargetStrategy strategy() {
        return switch (this) {
            case EASY -> TargetStrategy.RandomStrategy();
            case MEDIUM -> TargetStrategy.RandomAndHuntStrategy();
            case HARD -> TargetStrategy.ProbabilityAndHuntStrategy();
        };
    }
}
