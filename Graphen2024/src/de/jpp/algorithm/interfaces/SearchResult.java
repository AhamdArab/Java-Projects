package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SearchResult<N, A> {

    /**
     * Returns the status of the specified node
     *
     * @param node the node
     * @return the statuf of the specified node
     */
    NodeStatus getNodeStatus(N node);

    /**
     * Returns the predecessor of a path to the specified node (if already calculated)
     *
     * @param node the node
     * @return the predecessor of a path to the specified node (if already calculated)
     */
    Optional<Edge<N, A>> getPredecessor(N node);

    /**
     * Returns all known (OPEN or CLOSED) nodes in this search
     *
     * @return all known (OPEN or CLOSED) nodes in this search
     */
    Collection<N> getAllKnownNodes();


    /**
     * Returns all OPEN nodes in this search
     *
     * @return all OPEN nodes in this search
     */
    Collection<N> getAllOpenNodes();

    /**
     * Sets the status of the specified node to CLOSED
     *
     * @param node the node
     */
    void setClosed(N node);

    /**
     * Sets the status of the specified node to OPEN
     *
     * @param node
     */
    void setOpen(N node);

    /**
     * Removes all information from this SearchResult
     */
    void clear();

    /**
     * Returns the path to the specified destination (if calculated) or an empty Optional
     *
     * @param dest the destination
     * @return the path to the specified destination (if calculated) or an empty Optional
     */
    Optional<List<Edge<N, A>>> getPathTo(N dest);

}
