package de.jpp.model.interfaces;

import java.util.function.Consumer;

public interface GraphWithObservableModel<N, A> extends Graph<N, A> {

    /**
     * Adds the specified listener which is called whenever a node is added to the graph
     *
     * @param listener the listener
     */
    void addNodeAddedListener(Consumer<N> listener);

    /**
     * Adds the specified listener which is called whenever a node is removed from the graph
     *
     * @param listener the listener
     */
    void addNodeRemovedListener(Consumer<N> listener);

    /**
     * Adds the specified listener which is called whenever a edge is added to the graph
     *
     * @param listener the listener
     */
    void addEdgeAddedListener(Consumer<Edge<N, A>> listener);

    /**
     * Adds the specified listener which is called whenever a edge is removed from the graph
     *
     * @param listener the listener
     */
    void addEdgeRemovedListener(Consumer<Edge<N, A>> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeNodeAddedListener(Consumer<N> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeNodeRemovedListener(Consumer<N> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeEdgeAddedListener(Consumer<Edge<N, A>> listener);

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    void removeEdgeRemovedListener(Consumer<Edge<N, A>> listener);

}
