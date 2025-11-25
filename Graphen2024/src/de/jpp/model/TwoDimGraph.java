package de.jpp.model;

import de.jpp.model.interfaces.*;

import java.util.Optional;

/**
 * A Two Dimensional graph. <br>
 * The abstract-tag is only set because the tests will not compile otherwise. You should remove it!
 */
public class TwoDimGraph extends ObservableWeightedGraphImpl<XYNode, Double> implements WeightedGraph<XYNode, Double> {

    /**
     * Adds an edge to the graph which is automatically initialised with the euclidian distance of the nodes <br>
     * Returns the newly created edge
     *
     * @param start the start node of the edge
     * @param dest  the destination node of the edge
     * @return the newly created edge
     */

    public Edge<XYNode, Double> addEuclidianEdge(XYNode start, XYNode dest) {
        return addEdge(start, dest, Optional.of(start.euclidianDistTo(dest)));
    }

    @Override
    public double getDistance(Edge<XYNode, Double> edge) {
        return edge.getAnnotation().orElse(0.0);
    }

}

