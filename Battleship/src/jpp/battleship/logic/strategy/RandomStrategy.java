package jpp.battleship.logic.strategy;

import jpp.battleship.logic.BoardState;
import jpp.battleship.model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStrategy implements TargetStrategy{
    Random random = new Random();

    @Override
    public Coordinate next(BoardState state) {
        List<Coordinate> targets = new ArrayList<>(state.availableTargets());
        if (targets.isEmpty()) {
            throw new IllegalStateException();
        }
        return targets.get(random.nextInt(targets.size()));
    }

    @Override
    public String name() {
        return "Random";
    }
}
