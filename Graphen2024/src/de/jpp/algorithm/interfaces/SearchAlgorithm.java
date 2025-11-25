package de.jpp.algorithm.interfaces;

import de.jpp.factory.SearchStopFactory;
import de.jpp.model.interfaces.Graph;

public interface SearchAlgorithm<N, A, G extends Graph<N, A>> {

    /**
     * Starts the search process. Stops the search with the specified strategy
     *
     * @param type
     * @return
     */
    SearchResult<N, A> findPaths(SearchStopStrategy<N> type);

    /**
     * Starts the search process with a non-stopping search strategy
     *
     * @return the result of the search algorithm
     */
    SearchResult<N, A> findAllPaths();

    /**
     * Returns the observable search result so listener can be added before executing the search
     *
     * @return the observable search result
     */
    ObservableSearchResult<N, A> getSearchResult();

    /**
     * Returns the start node of this search
     *
     * @return the start node of this search
     */
    N getStart();

    /**
     * Returns the graph on which to search
     *
     * @return the graph on which to search
     */
    G getGraph();

    /**
     * This method stops the search. No further nodes will be added to the open or closed list, the searchResult won´t change anymore and no listener must be called after this method
     */
    void stop();


}
