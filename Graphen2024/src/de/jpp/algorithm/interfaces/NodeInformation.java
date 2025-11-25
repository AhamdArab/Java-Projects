package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;

import java.util.Objects;

public record NodeInformation<N, A>(Edge<N, A> predecessor, double dist) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeInformation<?, ?> that = (NodeInformation<?, ?>) o;
        return Double.compare(that.dist, dist) == 0 && Objects.equals(predecessor, that.predecessor);
    }

}
