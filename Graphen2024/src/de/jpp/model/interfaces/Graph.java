package de.jpp.model.interfaces;

import java.util.Collection;
import java.util.Optional;

public interface Graph<N, A> {


    /**
     * Ensures the node is part of this graph
     *
     * @param node the node to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    boolean addNode(N node);

    /**
     * Ensures that all nodes in this collection are part of this graph
     *
     * @param nodes the nodes to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    boolean addNodes(Collection<? extends N> nodes);

    /**
     * Ensures that all nodes in this collection are part of this graph
     *
     * @param nodes the nodes to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    boolean addNodes(N... nodes);

    /**
     * Returns all nodes in this graph
     *
     * @return all nodes in this graph
     */
    Collection<N> getNodes();

    /**
     * Ensures that a directed edge between the specified nodes are part of the graph <br>
     * If a node is not part of this graph, it will be added automatically
     *
     * @param start       the starting point of the edge
     * @param destination the destination point of this edge.
     * @param annotation  annotations to this edge
     * @return the instance of the newly created edge
     */
    Edge<N, A> addEdge(N start, N destination, Optional<A> annotation);

    /**
     * Ensures that a directed edge without annotation is part of this graph <br>
     * If a node is not part of this graph, it will be added automatically
     *
     * @param start       the starting point of the edge
     * @param destination the destination point of this edge.
     * @return the instance of the newly created edge
     */
    default Edge<N, A> addEdge(N start, N destination) {
        return addEdge(start, destination, Optional.empty());
    }


    /**
     * Ensures that the specified edge is no longer part of this graph
     *
     * @param edge the edge to be removed
     * @return true if this operation changed the model of this graph
     */
    boolean removeEdge(Edge<N, A> edge);

    /**
     * Returns all Edges starting at the specified node.
     *
     * @param node the start node of every edge in this collection
     * @return every edge of the graph starting at this node
     */
    Collection<Edge<N, A>> getNeighbours(N node);

    /**
     * Returns all Edges ending at the specified node
     *
     * @param node the destination node of every edge in this collection
     * @return every edge of the graph ending at this node
     */
    Collection<Edge<N, A>> getReachableFrom(N node);

    /**
     * Returns all edges in this graph
     *
     * @return all edges in this graph
     */
    Collection<Edge<N, A>> getEdges();


    /**
     * Ensures that the specified Node is no longer part of this graph <br>
     * Removes all edges containing the specified node.
     *
     * @param node the node to be deleted
     * @return true if this operation changed the model of this graph
     */
    boolean removeNode(N node);

    /**
     * Ensures that the specified Nodes are no longer part of this graph <br>
     * Removes all edges containing the specified nodes.
     *
     * @param nodes the nodes to be deleted
     * @return true if this operation changed the model of this graph
     */
    boolean removeNodes(Collection<? extends N> nodes);

    /**
     * Ensures that the specified Nodes are no longer part of this graph <br>
     * Removes all edges containing the specified nodes.
     *
     * @param nodes the nodes to be deleted
     * @return true if this operation changed the model of this graph
     */
    boolean removeNodes(N... nodes);


    /**
     * Ensures that no edges or nodes are part of this graph anymore.
     */
    void clear();

}
