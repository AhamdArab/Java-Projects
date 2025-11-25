package de.jpp.model.interfaces;

public interface WeightedGraph<N, A> extends Graph<N, A> {

    /**
     * Returns the weight of the specified edge
     *
     * @param edge the edge
     * @return the weight of the specified edge
     */
    double getDistance(Edge<N, A> edge);

}
