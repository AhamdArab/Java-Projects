package de.jpp.factory;

import de.jpp.algorithm.StartToDestStrategy;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.algorithm.interfaces.SearchStopStrategyImpl;

public class SearchStopFactory {

    /**
     * Returns a SearchStopStrategy which never stops on its own
     *
     * @param <N> the exact types of nodes in the graph underlying the search
     * @return a SearchStopStrategy which never stops on its own
     */
    public <N> SearchStopStrategy<N> expandAllNodes() {
        return new SearchStopStrategy<N>() {
            @Override
            public boolean stopSearch(N lastClosedNode) {
                return false;
            }
        };
    }

    /**
     * Returns a SearchStopStrategy which stops after a specified number of nodes are CLOSED
     *
     * @param <N>      the exact type of nodes in the graph underlying the search
     * @param maxCount the maximum number of nodes which may be CLOSED
     * @return a SearchStopStrategy which stops after a specified number of nodes are CLOSED
     */
    public <N> SearchStopStrategyImpl maxNodeCount(int maxCount) {
        return new SearchStopStrategyImpl(maxCount);
    }

    /**
     * Returns a SearchStopStrategy which stops after a specified destination has been reached
     *
     * @param dest the destination
     * @param <N>  the exact type of nodes in the graph underlying the search
     * @return a SearchStopStrategy which stops after a specified destination has been reached
     */
    public <N> StartToDestStrategy<N> startToDest(N dest) {
        return new StartToDestStrategy<>(dest);
    }

}
