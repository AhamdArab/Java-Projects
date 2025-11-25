package jpp.battleship.logic.strategy;

import jpp.battleship.logic.BoardState;
import jpp.battleship.model.Alignment;
import jpp.battleship.model.Coordinate;
import jpp.battleship.model.ShipClass;

import java.util.*;

public class ProbabilityAndHuntStrategy implements TargetStrategy {
    RandomAndHuntStrategy huntStrategy = new RandomAndHuntStrategy();

    @Override
    public Coordinate next(BoardState state) {
        if (!state.getDamaged().isEmpty()) {
            return huntStrategy.next(state);
        }

        int width = state.getWidth();
        int height = state.getHeight();
        Set<Coordinate> availableTargets = state.availableTargets();
        if (availableTargets.isEmpty()) {
            throw new IllegalStateException();
        }

        Map<Coordinate, Integer> scoreMap = new HashMap<>();
        for (Coordinate c : availableTargets) {
            scoreMap.put(c, 0);
        }

        for (ShipClass shipClass : state.remainingShipClasses()) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Coordinate ref = new Coordinate(x, y);
                    for (Alignment alignment : new Alignment[]{Alignment.HORIZONTAL, Alignment.VERTICAL}) {
                        Set<Coordinate> offsets = shipClass.points(alignment);
                        Set<Coordinate> placement = new HashSet<>();
                        boolean validPlacement = true;
                        for (Coordinate offset : offsets) {
                            Coordinate pos = ref.add(offset);
                            if (pos.getX() < 0 || pos.getX() >= width || pos.getY() < 0 || pos.getY() >= height) {
                                validPlacement = false;
                                break;
                            }
                            if (!availableTargets.contains(pos)) {
                                validPlacement = false;
                                break;
                            }
                            placement.add(pos);
                        }
                        if (validPlacement) {
                            for (Coordinate pos : placement) {
                                scoreMap.put(pos, scoreMap.get(pos) + 1);
                            }
                        }
                    }
                }
            }
        }

        Coordinate bestTarget = null;
        int maxScore = -1;
        for (Map.Entry<Coordinate, Integer> entry : scoreMap.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestTarget = entry.getKey();
            }
        }

        if (bestTarget == null) {
            bestTarget = availableTargets.iterator().next();
        }
        return bestTarget;
    }

    @Override
    public String name() {
        return "ProbabilityAndHunt";
    }

}