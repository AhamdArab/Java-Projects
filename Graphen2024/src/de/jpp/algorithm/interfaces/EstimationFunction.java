package de.jpp.algorithm.interfaces;

public interface EstimationFunction<N> {


    /**
     * Returns the estimated distance between the two nodes
     *
     * @param node        the first node
     * @param destination the second node
     * @return the estimated distance between the two nodes
     */
    double getEstimatedDistance(N node, N destination);


}
