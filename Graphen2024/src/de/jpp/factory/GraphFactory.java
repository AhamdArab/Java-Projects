package de.jpp.factory;

import de.jpp.model.LabelMapGraph;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.GraphenImpl;
import de.jpp.model.interfaces.ObservableGraph;
import de.jpp.model.interfaces.ObservableWeightedGraphImpl;

public class GraphFactory {

    /**
     * Creates a new and empty graph Instance which is not an ObservableGraph
     *
     * @param <N> the class type of nodes in this graph
     * @param <A> the annotation type of edges in this graph
     * @return a new and empty Graph instance
     */
    public <N, A> Graph<N, A> createNewGraph() {
        return new GraphenImpl<>();
    }

    /**
     * Creates a new and empty graph Instance which is also an ObservableGraph
     *
     * @param <N> the class type of nodes in this graph
     * @param <A> the annotation type of edges in this graph
     * @return a new and empty Graph instance
     */
    public <N, A> ObservableGraph<N, A> createNewObservableGraph() {
        return new ObservableWeightedGraphImpl<>();
    }

    /**
     * Creates a new and empty TwoDimGraph instance
     *
     * @return a new and empty TwoDimGraph instance
     */
    public TwoDimGraph createNewTwoDimGraph() {
        return new TwoDimGraph();
    }

    public LabelMapGraph createNewLabelMapGraph() {
        return new LabelMapGraph();
    }

}
