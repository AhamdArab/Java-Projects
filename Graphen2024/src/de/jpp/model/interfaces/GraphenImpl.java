package de.jpp.model.interfaces;

import java.util.*;
import java.util.stream.Collectors;

public class GraphenImpl<N, A> implements Graph<N, A>{

    List<N> nodes = new ArrayList<>();
    Map<N, List<Edge<N, A>>> edges = new HashMap<>();

    @Override
    public boolean addNode(N node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
            edges.put(node, new ArrayList<>());
            return true;
        }
        return false;
    }

    @Override
    public boolean addNodes(Collection<? extends N> nodes) {
        return nodes.stream().map(this::addNode)
                .reduce(Boolean::logicalOr).orElse(false);

    }

    @SafeVarargs
    @Override
    public final boolean addNodes(N... nodes) {
        return addNodes(Arrays.asList(nodes));
    }

    @Override
    public Collection<N> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation) {
        addNode(start);
        addNode(destination);
        Edge<N, A> edge = new Edge<>(start, destination, annotation);
        ensureEdgeListNonNull(start).add(edge);
        return edge;
    }

    @Override
    public boolean removeEdge(Edge<N, A> edge) {
        if (edge == null) {
            return false;
        }
        boolean removed = false;
        List<Edge<N, A>> edgeList = edges.get(edge.getStart());
        if (edgeList != null) {
            removed = edgeList.remove(edge);
        }
        return removed;
    }

    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {
        return List.copyOf(ensureEdgeListNonNull(node));
    }

    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {
        return edges.values().stream()
                .flatMap(Collection::stream)
                .filter(edge -> edge.getDestination().equals(node))
                .collect(Collectors.toList());
    }


    @Override
    public Collection<Edge<N, A>> getEdges() {
        return edges.values().stream().flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeNode(N node) {
        if (nodes.remove(node)) {
            edges.remove(node);
            removeHook(node);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeNodes(Collection<? extends N> nodes) {
        boolean changed = false;
        for (N node : nodes) {
            changed |= removeNode(node);
        }
        return changed;
    }

    @SafeVarargs
    @Override
    public final boolean removeNodes(N... nodes) {
        return removeNodes(Arrays.asList(nodes));
    }

    @Override
    public void clear() {
        nodes.clear();
        edges.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphenImpl<?, ?> other)) return false;

        if (this.nodes.size() != other.nodes.size()) return false;

        if (!new HashSet<>(this.nodes).containsAll(other.nodes) || !new HashSet<>(other.nodes).containsAll(this.nodes)) return false;


        List<Edge<N, A>> thisEdges = this.edges.values().stream().flatMap(List::stream).toList();
        List<?> otherEdges = other.edges.values().stream().flatMap(List::stream).toList();

        if (thisEdges.size() != otherEdges.size()) return false;

        return new HashSet<>(thisEdges).containsAll(otherEdges) && new HashSet<>(otherEdges).containsAll(thisEdges);
    }

    @Override
    public int hashCode() {
        int result = nodes.hashCode();
        result = 31 * result + edges.keySet().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GraphenImpl{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    private void removeHook(N node) {
        edges.forEach((key, value) -> value.removeIf(edge -> edge.getStart().equals(node) || edge.getDestination().equals(node)));
    }

    private List<Edge<N, A>> ensureEdgeListNonNull(N node) {
        return edges.computeIfAbsent(node, k -> new ArrayList<>());
    }
}
