package de.jpp.model.interfaces;

import java.util.Collection;
import java.util.function.Consumer;

public interface GraphWithObservableQueries<N, A> extends Graph<N, A> {


    /**
     * Adds the specified listener which is called whenever graph.getNeighbours(...) is called
     *
     * @param listener the listener
     */
    void addNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener);

    /**
     * Adds the specified listener which is called whenever graph.getReachable(...) is called
     *
     * @param listener the listener
     */
    void addReachableListedListener(Consumer<Collection<Edge<N, A>>> listener);

    /**
     * Adds the specified listener which is called whenever graph.getNodes() is called
     *
     * @param listener the listener
     */
    void addNodesListedListener(Consumer<Collection<N>> listener);


    /**
     * Adds the specified listener which is called whenever graph.getEdges() is called
     *
     * @param listener the listener
     */
    void addEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener);


    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeReachableListedListener(Consumer<Collection<Edge<N, A>>> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeNodesListedListener(Consumer<Collection<N>> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener);

}
