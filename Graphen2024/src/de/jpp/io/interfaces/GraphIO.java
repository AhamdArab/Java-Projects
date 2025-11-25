package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Graph;

public interface GraphIO<N, A, G extends Graph<N, A>, F> extends GraphReader<N, A, G, F>, GraphWriter<N, A, G, F>{



}
