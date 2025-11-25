package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.SearchStopStrategy;


public record StartToDestStrategy<N>(N dest) implements SearchStopStrategy<N> {

    @Override
    public boolean stopSearch(N lastClosedNode) {
        return dest == lastClosedNode;
    }

    /**
     * Returns the destination node of this search
     *
     * @Returns the destination node of this search
     */
    @Override
    public N dest() {
        return dest;
    }

}
