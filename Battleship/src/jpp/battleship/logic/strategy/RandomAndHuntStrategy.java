package jpp.battleship.logic.strategy;

import jpp.battleship.logic.BoardState;
import jpp.battleship.model.Alignment;
import jpp.battleship.model.Coordinate;

import java.util.*;

public class RandomAndHuntStrategy implements TargetStrategy{
    RandomStrategy randomStrategy = new RandomStrategy();

    @Override
    public Coordinate next(BoardState state) {
        List<Coordinate> damaged = state.getDamaged();
        if (damaged.isEmpty()) {
            return randomStrategy.next(state);
        }


        Set<Coordinate> available = state.availableTargets();
        Set<Coordinate> targets = new HashSet<>();
        for (Coordinate hit : damaged) {
            for (Coordinate neighbor : getNeighbors(hit, state)) {
                if (available.contains(neighbor)) {
                    targets.add(neighbor);
                }
            }
        }

        if (targets.isEmpty()) {
            return randomStrategy.next(state);
        }

        if (damaged.size() == 1) {
            return targets.iterator().next();
        }

        Coordinate last = damaged.get(damaged.size() - 1);
        Coordinate secondLast = damaged.get(damaged.size() - 2);
        Optional<Alignment> alignmentOpt = secondLast.computeAlignment(last);

        if (alignmentOpt.isPresent()) {
            Alignment alignment = alignmentOpt.get();
            List<Coordinate> alignedTargets = targets.stream()
                    .filter(candidate -> {
                        if (alignment == Alignment.HORIZONTAL) {
                            return candidate.getY() == last.getY();
                        } else {
                            return candidate.getX() == last.getX();
                        }
                    }).toList();
            if (!alignedTargets.isEmpty()) {
                return alignedTargets.get(0);
            }
        }

        return targets.iterator().next();
    }

    @Override
    public String name() {
        return "RandomAndHunt";
    }


    private List<Coordinate> getNeighbors(Coordinate coord, BoardState state) {
        List<Coordinate> neighbors = new ArrayList<>();
        int x = coord.getX();
        int y = coord.getY();


        if (y > 0) {
            neighbors.add(new Coordinate(x, y - 1));
        }

        if (y < state.getHeight() - 1) {
            neighbors.add(new Coordinate(x, y + 1));
        }

        if (x > 0) {
            neighbors.add(new Coordinate(x - 1, y));
        }

        if (x < state.getWidth() - 1) {
            neighbors.add(new Coordinate(x + 1, y));
        }
        return neighbors;
    }


}
