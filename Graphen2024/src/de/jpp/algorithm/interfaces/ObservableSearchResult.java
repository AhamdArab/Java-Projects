package de.jpp.algorithm.interfaces;

import java.util.function.BiConsumer;

public interface ObservableSearchResult<N, A> extends SearchResult<N, A> {


    /**
     * Adds a listener to the search result which is called whenever a new node is opened
     *
     * @param onOpen the listener to be added to the search result
     */
    void addNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen);

    /**
     * removes the specified listener from the search result
     *
     * @param onOpen the listener
     */
    void removeNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen);

    /**
     * Adds a listener to the search result which is called whenever a new node is closed
     *
     * @param onClose
     */
    void addNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose);

    /**
     * removes the specified listener from the search result
     *
     * @param onClose
     */
    void removeNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose);

}
