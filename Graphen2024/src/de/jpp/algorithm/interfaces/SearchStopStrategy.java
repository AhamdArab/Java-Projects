package de.jpp.algorithm.interfaces;

public interface SearchStopStrategy<N> {


    /**
     * Returns whether the search should be stopped after expanding the specified node
     *
     * @param lastClosedNode the node
     * @return whether the search should be stopped after expanding the specified node
     */
    boolean stopSearch(N lastClosedNode);


}


