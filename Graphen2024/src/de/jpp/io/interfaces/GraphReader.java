package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Graph;

public interface GraphReader<N, A, G extends Graph<N, A>, F> {

    /**
     * Creates a graph from the specified input source
     * @param input the input source
     * @return a graph from the input source
     * @throws ParseException if the input cannot be parsed correctly
     */
    G read(F input) throws ParseException;


}
