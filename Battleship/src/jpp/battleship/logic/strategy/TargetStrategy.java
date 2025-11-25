package jpp.battleship.logic.strategy;

import jpp.battleship.logic.BoardState;
import jpp.battleship.model.Coordinate;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Set;

public interface TargetStrategy {
    public Coordinate next(BoardState state);

    public String name();

    public default Coordinate get(BoardState state) {
        Objects.requireNonNull(state);
        Set<Coordinate> targets = state.availableTargets();
        if (targets.isEmpty()) {
            throw new IllegalStateException();
        }
        return next(state);
    }

    public static TargetStrategy RandomStrategy(){
        return new RandomStrategy();
    }

    public static TargetStrategy RandomAndHuntStrategy(){
        return new RandomAndHuntStrategy();
    }

    public static TargetStrategy ProbabilityAndHuntStrategy(){
        return new ProbabilityAndHuntStrategy();
    }

    public static TargetStrategy UserChoiceStrategy(InputStream inputStream, OutputStream outputStream){
        return new UserChoiceStrategy(inputStream, outputStream);
    }
}
