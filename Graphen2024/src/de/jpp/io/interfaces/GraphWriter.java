package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Graph;

public interface GraphWriter<N, A, G extends Graph<N, A>, F> {

    /**
     * Creates the output from the specified graph
     * @param graph the graph
     * @return the output from the specified graph
     */
    F write(G graph);

}
